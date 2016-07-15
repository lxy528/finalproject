import MySQLdb
import string
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

#color = ['b','g','r','c','m','y','k','b--','g--','r--','c--','m--','y--','k--']
fs = 28
'''
f = open('Create-Snippet-gist.txt','w')
Con= MySQLdb.connect(host='42.96.151.83',user='jwx',passwd='jwx',db='gist')
#Con= MySQLdb.connect(host='localhost',user='root',passwd='',db='snippets')
cursor =Con.cursor()
sql ="SELECT creator,COUNT(*) FROM end_snippet_cache where ide = '' GROUP BY creator ORDER BY COUNT(creator)"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(r[0]+" "+str(r[1])+"\n");
print "gist is over"


f = open('Create-Snippet-csdn.txt','w')
sql ="SELECT creator,COUNT(*) FROM end_snippet_cache where ide = 'csdn' GROUP BY creator ORDER BY COUNT(creator)"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(r[0]+" "+str(r[1])+"\n");
print "csdn is over"

cursor.close()
Con.close()
'''





	
z = [0,2000,4000,6000,8000,10000]
horizontal = [1,2,3,4,5,6,7,8,9,10,11]
data_gist = [0,0,0,0,0,0,0,0,0,0]
f = open('data/Create-Snippet-gist.txt','r')
for line in f:
	d = line.split(' ')[1].strip(' /n')
	n = int(d)
	for i in range(len(horizontal)-1):
		if n >= horizontal[i] and n < horizontal[i+1]:
			data_gist[i] += 1
#for t in data_gist:
#	print t
	
horizontal = [1,2,3,4,5,6,7,8,9,10,11]
data_csdn = [0,0,0,0,0,0,0,0,0,0]
f = open('data/Create-Snippet-csdn.txt','r')
for line in f:
	d = line.split(' ')[1].strip(' /n')
	n = int(d)
	for i in range(len(horizontal)-1):
		if n >= horizontal[i] and n < horizontal[i+1]:
			data_csdn[i] += 1
#for t in data_csdn:
#	print t
	
	
plt.figure(figsize = (14,7))  
ax = plt.subplot(111)   

mpl.rc('xtick', labelsize=10)
mpl.rc('ytick', labelsize=10)
#plt.grid(True)
ind = np.arange(len(data_csdn))+ 0 # the x locations for the groups
#ind = 0.1
width = 0.4       # the width of the bars

rects1 = ax.bar(ind, data_csdn, width, color='g')
rects2 = ax.bar(ind+width, data_gist, width, color='y')



ax.set_ylabel('Number of Users',fontsize=fs)
ax.set_xlabel('Number of Snippets',fontsize=fs)
#ax.set_title("The Maximum and Average number of lines of code fragments",fontsize=20)
ax.set_ylim(0,10000)                 
ax.set_xticks(ind+width)
ax.set_xticklabels( horizontal,rotation = 0,fontsize=fs )
ax.set_yticklabels(z,fontsize=fs )

plt.legend( (rects1[0], rects2[0]), ('code@CSDN','gist@GitHub'), loc='upper right',fontsize=fs)

def autolabel(rects):
    # attach some text labels
    for rect in rects:
		height = rect.get_height()
		ax.text(rect.get_x()+rect.get_width()/2., 1.01*height, '%d'%int(height),
			ha='center', va='bottom',size=16)
autolabel(rects1)
autolabel(rects2)
#plt.savefig('3_1.png') 
plt.show()
