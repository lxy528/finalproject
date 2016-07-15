import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import datetime
import MySQLdb

'''
f = open('data/description-Snippet-gist.txt','w')
Con= MySQLdb.connect(host='42.96.151.83',user='jwx',passwd='jwx',db='gist')
#Con= MySQLdb.connect(host='localhost',user='root',passwd='',db='snippets')
cursor =Con.cursor()
sql ="SELECT snippet_id,LENGTH(`comment`)-LENGTH(REPLACE(`comment`,' ','')) AS len FROM `end_snippet_cache` WHERE ide = '' ORDER BY len DESC"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(str(r[0])+" "+str(r[1])+"\n");
print "gist is over"
f.close()
cursor.close()
Con.close()
'''

z = [i*500 for i in range(9)]
horizontal = [i for i in range(25)]
data_gist = [0 for i in range(25)]

f = open('data/description-Snippet-gist.txt','r')
for line in f:
	d = line.split(' ')[1].strip(' ')
	n = int(d)
	for i in range(len(horizontal)-1):
		if n >= horizontal[i] and n < horizontal[i+1]:
			data_gist[i] += 1
for t in data_gist:
	print t
f.close()
'''
f = open('Language-Snippet-csdn.txt','w')
Con= MySQLdb.connect(host='42.96.151.83',user='jwx',passwd='jwx',db='gist')
#Con= MySQLdb.connect(host='localhost',user='root',passwd='',db='snippets')
cursor =Con.cursor()
sql ="SELECT `language`,COUNT(*) as count FROM `end_snippet_cache` WHERE ide = 'csdn' AND `language` <> '' GROUP BY `language` ORDER BY count desc"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(r[0]+" "+str(r[1])+"\n");
print "csdn is over"

'''
fs = 28
fx = 22
plt.figure(figsize = (16,9))  
ax = plt.subplot(111)   

mpl.rc('xtick', labelsize=10)
mpl.rc('ytick', labelsize=10)
#plt.grid(True)
ind = np.arange(len(data_gist))+ 0 # the x locations for the groups
#ind = 0.1
width = 1       # the width of the bars

rects1 = ax.bar(ind, data_gist, width, color='g')
#rects2 = ax.bar(ind+width, data_gist, width, color='y')



ax.set_ylabel('Number of Snippets',fontsize=fs)
ax.set_xlabel('Number of Words',fontsize=fs)
#ax.set_title("The Maximum and Average number of lines of code fragments",fontsize=20)
ax.set_ylim(0,4000)                 
ax.set_xticks(ind+width*0.5)
ax.set_xticklabels( horizontal,rotation = 0,fontsize=fx )
ax.set_yticklabels(z,fontsize=fs )

#plt.legend( (rects1[0], rects2[0]), ('csdn','gist'), loc='upper right',fontsize=fs)

def autolabel(rects):
    # attach some text labels
    for rect in rects:
        height = rect.get_height()
        ax.text(rect.get_x()+rect.get_width()/2., 1.01*height, '%d'%int(height),
                ha='center', va='bottom',size=fs)
#autolabel(rects1)
#autolabel(rects2)
#plt.savefig('3_1.png') 
plt.show()
