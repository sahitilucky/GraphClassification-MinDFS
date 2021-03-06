from mpl_toolkits.mplot3d import *
import matplotlib.pyplot as plt
from matplotlib import cm
import random as rd
import math
f=open("alltimes","r")
line=f.readline()
X=[]
Y=[]
while line:
	line=line.split(" ")
	X=X+[float(line[0])]
	Y=Y+[float(line[1])]	
	line=f.readline()

print X
print Y
plt.xlabel('Percentage Minsupport ')
plt.ylabel('Time in seconds')
plt.hold(True)

plt.plot(X[:5],Y[:5],'bo')
plt.plot(X[:5],Y[:5],linewidth=2.0,label='Gaston')
plt.plot(X[5:10],Y[5:10],'rx')
plt.plot(X[5:10],Y[5:10],linewidth=2.0,label='Gspan')
plt.plot(X[10:15],Y[10:15],'gx')
plt.plot(X[10:15],Y[10:15],linewidth=2.0,label='FSG')


plt.legend()
plt.savefig("Minsup Vs Time")
plt.close()
