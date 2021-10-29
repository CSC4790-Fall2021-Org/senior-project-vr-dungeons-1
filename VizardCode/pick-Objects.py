import viz
import vizconnect
import vizshape
import vizact

viz.setMultiSample(4)
vizconnect.go('C:/Users/Christina LaRow/Downloads/config.py')

viz.phys.enable()

# Add a background environment
dojo = viz.addChild('dojo.osgb')