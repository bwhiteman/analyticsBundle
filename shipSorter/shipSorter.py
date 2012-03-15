#!/usr/bin/python
#
# main.py
# Copyright (C) Dave Page 2012 <dpage@mytoy>
# 
#python-foobar is free software: you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# python-foobar is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License along
# with this program.  If not, see <http://www.gnu.org/licenses/>.


import sys
import csv
import math
import string

#defined functions:
_PI		= 3.1415926
def subInt( text, start=0, end=2):
	if( len(text) >= end):
		return( int( text[start:end]))
	return( 0)
	
def timeToHours ( time):
	moe		= [0,31,60,91,121,152,182,213,244,274,305,335]
	moo		= [0,31,59,90,120,151,181,212,243,273,304,334]

	total	= -1
	time	= time.strip()
	year	= subInt( time,0,2)
	if( year >= 12):
		mot	= moo
		if( ( year & 3) == 0):
			mot	= moe

		month	= subInt( time,2,4)
		day		= subInt( time,4,6)
		if( ( month >= 1) and ( day >= 1)):
			hour	= subInt( time,6,8)
			minute	= subInt( time,8,10)
			total	= minute + hour*60 + ( ( day-1) + mot[ month-1])*60*24
			#print "date time ="+str(total),str(year), str(month), str(hour), str(day), str(hour), str(minute)
	return( total)
	
def degToRadian( deg = 0.0):
	return( ( deg / 180.0) * _PI)

def radianToDeg( radian = 0.0):
	return( ( radian / _PI) * 180.0)

def distance( prevLat, prevLon, atLat, atLon):
		R = 6371.0	# km
		dLat = degToRadian( atLat - prevLat)
		dLon = degToRadian( atLon - prevLon)
		lat1 = degToRadian( prevLat)
		lat2 = degToRadian( atLat)

		a = math.sin(dLat/2) * math.sin(dLat/2) + math.sin(dLon/2) * math.sin(dLon/2) * math.cos(lat1) * math.cos(lat2) 
		c = 2 * math.atan2( math.sqrt( a), math.sqrt( 1-a))
		distance = R * c
		#print "From To:",  prevLat, prevLon, atLat, atLon,a,c,d
		
		return( distance)

def course( prevLat, prevLon, atLat, atLon):
		dLat = degToRadian( atLat - prevLat)
		dLon = degToRadian( atLon - prevLon)
		lat1 = degToRadian( prevLat)
		lat2 = degToRadian( atLat)

		y = math.sin(dLon) * math.cos(lat2);
		x = math.cos(lat1) * math.sin(lat2) - math.sin(lat1) * math.cos(lat2) * math.cos(dLon);
		bearng = radianToDeg( math.atan2(y, x));
		
		return( bearng)

def openKml( kmlName):
		kmlFileName	= kmlName.replace( ' ', '_') + ".kml"
		fileHandle	= open( kmlFileName, "wb")
		fileHandle.write ( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
		fileHandle.write ( "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n")
		fileHandle.write ( "<Document>\n<StyleMap id=\"m_ylw-pushpin\">\n<Pair>\n<key>normal</key>\n<styleUrl>#s_ylw-pushpin</styleUrl>\n</Pair>\n<Pair>\n<key>highlight</key>\n<styleUrl>#s_ylw-pushpin_hl</styleUrl>\n</Pair>\n</StyleMap>")
		fileHandle.write ( "<Placemark>\n<name>"+kmlName+"</name>\n<styleUrl>#m_ylw-pushpin</styleUrl>\n<LineString>\n<tessellate>1</tessellate>\n<coordinates>\n")
		return( fileHandle)

def addKml( handle, lat, lon):
		handle.write ( lon+","+lat+",0  ")
		
def closeKml( handle):
		handle.write ( "</coordinates>\n</LineString>\n</Placemark>\n</Document>\n</kml>\n\n")
		handle.close()

#end defined functions
fileName	= "aisShipData.csv"
shipName	= "ADEONA"
if( len(sys.argv) > 1):
    fileName	= sys.argv[1]
if( len(sys.argv) > 2):
	shipName	= string.upper( sys.argv[2])

	
kmlFile		= openKml( shipName)
outName		= shipName.replace( ' ', '_') + ".csv"
shipName	= "'"+shipName+"'"

csvfile		= open( fileName, "rU")
csvOutfile	= open( outName, "wb+")

spamReader = csv.reader( csvfile, dialect=csv.excel)
spamWriter = csv.writer( csvOutfile, dialect=csv.excel)
print "csv Read!"

total		= 0

lastLat		= -200.0
lastLon		= -200.0
lastTime	= 0
for row in spamReader:
	index	= 0
	total	= total + 1

	if( row[ index+2].strip() == shipName.strip()):
		if( row[ index].strip() == "ais(1.0)"):
			timeIndex	= timeToHours( row[ index+8])

			if( timeIndex > lastTime):
				curLat		= float( row[ index+4].strip())
				curLon		= float( row[ index+5].strip())
				if( lastLat < -90):
					lastLat		= curLat
					lastLon		= curLon
					lastTime	= timeIndex

				delta		= distance( lastLat, lastLon, curLat, curLon)
				bearing		= course( lastLat, lastLon, curLat, curLon)
				spamWriter.writerow( row + [ str( timeIndex)] + [ str( delta)] + [ str( bearing)] + [ str( timeIndex - lastTime)])

				addKml( kmlFile, str( curLat), str( curLon))
				
				lastLat		= curLat
				lastLon		= curLon
				lastTime	= timeIndex
			
				#for item in row:
				#	if( index > 0):
				#		print item
				#	index	+= 1

csvfile.close()
csvOutfile.close()
closeKml( kmlFile)
print "Done, Total Records Scanned =",total

