import viz
import vizact
import vizshape
import csv

viz.fov(90)
viz.go()

#vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

floor = vizshape.addQuad(size=(1.0,1.0),axis=vizshape.AXIS_Y,texture=viz.add("white.jpg"),lighting=False)
floor.setPosition([1,-1,0])

with open('../GeneratorCode/output.csv') as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]

layout = []

#print(data)


width = int(data.pop(0))
height = int(data.pop(0))

#print(data)
row = -1

for i in range(0,len(data)):
	#print(i)
	if(i%width == 0):
		layout.append([])
		row+=1
	layout[row].append(data[i])

#print(layout)

row = -1
col = -1

for r in layout:
	row+=1
	col=-1
	for entry in r:
		col+=1
		#print("entry = " + entry)
		if(entry=="false"):
			print("copied floor to ("+str(row)+","+str(col)+")")
			floor.copy().setPosition(row,1,col)
			
print("Done")
