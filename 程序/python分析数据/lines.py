import MySQLdb
import string
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

#color = ['b','g','r','c','m','y','k','b-.','g--','r--','c--','m--','y--','k--']

'''
f = open('lines-Snippet-gist.txt','w')
Con= MySQLdb.connect(host='42.96.151.83',user='jwx',passwd='jwx',db='gist')
#Con= MySQLdb.connect(host='localhost',user='root',passwd='',db='snippets')
cursor =Con.cursor()
sql ="SELECT snippet_id,`language`,LENGTH(`code`)-LENGTH(REPLACE(`code`,'\n','')) AS line FROM `end_snippet_cache` WHERE `language` <> '' AND ide = '' ORDER BY line"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(str(r[0])+" "+r[1]+" "+str(r[2])+"\n");
print "gist is over"


f = open('lines-Snippet-csdn.txt','w')
sql ="SELECT snippet_id,`language`,LENGTH(`code`)-LENGTH(REPLACE(`code`,'\n','')) AS line FROM `end_snippet_cache` WHERE `language` <> '' AND ide = 'csdn' ORDER BY line"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(str(r[0])+" "+r[1]+" "+str(r[2])+"\n");
print "csdn is over"

cursor.close()
Con.close()
'''

fs = 28
z = [i*1000 for i in range(10)]
horizontal = []
wid = 5
length = 101
data_gist = [0 for i in range(length-1)]
hor = []
for h in range(length):
	horizontal.append(h*wid)
	
for h in range(length/10 + 1):
	hor.append(h*wid*10)
	for y in range(9):
		hor.append('')
	
f = open('data/Lines-Snippet-gist.txt','r')

for line in f:
	l = line.split(' ')
	d = l[len(l)-1].strip(' /n')
	n = int(d)
	for i in range(length-1):
		if n >= horizontal[i] and n < horizontal[i+1]:
			data_gist[i] += 1

print "gist"
for t in data_gist:
	print t
data_csdn = [0 for i in range(length-1)]
f = open('data/Lines-Snippet-csdn.txt','r')
for line in f:
	l = line.split(' ')
	d = l[len(l)-1].strip(' ')
	n = int(d)
	for i in range(length-1):
		if n >= horizontal[i] and n < horizontal[i+1]:
			data_csdn[i] += 1
print "csdn"
for t in data_csdn:
	print t


plt.figure(figsize = (14,7))  
ax = plt.subplot(111)   

mpl.rc('xtick', labelsize=10)
mpl.rc('ytick', labelsize=10)
#plt.grid(True)
ind = np.arange(len(data_csdn))+ 0 # the x locations for the groups
#ind = 0.1
width = 1       # the width of the bars

rects1 = ax.bar(ind, data_csdn, width, color='g')
rects2 = ax.bar(ind, data_gist, width, color='y')



ax.set_ylabel('Number of Snippets',fontsize=fs)
ax.set_xlabel('Number of Lines',fontsize=fs)                
ax.set_xticks(ind)
ax.set_xticklabels( hor,rotation = 0,fontsize=fs )
ax.set_yticklabels(z,fontsize=fs )
plt.legend( (rects1[0], rects2[0]), ('code@CSDN','gist@GitHub'), loc='upper right',fontsize=fs)


plt.show()

