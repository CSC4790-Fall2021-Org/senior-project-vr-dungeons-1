import viz
import vizact
import vizshape
import vizconnect
from vizconnect.util import view_collision
import csv

viz.fov(90)
viz.go()

#vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

#creates the master floor tile from which every other tile will be cloned and sets it at position [1,-1,0], underneath the floor
floor = vizshape.addQuad(size=(1.0,1.0),axis=vizshape.AXIS_Y,texture=viz.add("white.jpg"),lighting=False)
floor.setPosition([1,-1,0])

#reads from the csv file in GeneratorCode rechange to open('../GeneatorCode/output.csv)
with open('../GeneratorCode/output.csv') as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]

layout = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width = int(data.pop(0))
height = int(data.pop(0))

#row will be used to count the rows, starting at 0 with the first row+=1
row = -1
for i in range(0,len(data)):
	#if we've reached the beginning of a new row, add a new list to the list
	if(i%width == 0):
		layout.append([])
		row+=1
	#add the data from the csv to the current row
	layout[row].append(data[i])

#row and col temp variables for counting, starting at 0 with the first row+=1 and col+=1
row = -1
col = -1

wall = vizshape.addBox(size=(1.0,4.0,1.0),texture=viz.addTexture("north.png"))

#column number where the entrance is
startPosition = 0

#iterate over every entry in the 2d list
for r in layout:
	row+=1
	col=-1
	for entry in r:
		col+=1
		#if there should be a floor at (row,col), clone the master floor to (row,1,col)
		if(entry=="false"):
			floor.copy().setPosition(row,1,col)
			
			if(row == 0): #finds the empty space in the first row the entrance)
				startPosition = col
				print(col)
		else:
			wall.copy().setPosition(row,3,col)

viz.MainView.setPosition([startPosition+3.5,2.8,-5]) #sets the start position to 10 feet behind the entrance	
viz.MainView.collision(viz.ON)		
print("Done")
