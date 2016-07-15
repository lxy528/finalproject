import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import datetime
import MySQLdb

'''
f = open('Language-Snippet-gist.txt','w')
Con= MySQLdb.connect(host='42.96.151.83',user='jwx',passwd='jwx',db='gist')
#Con= MySQLdb.connect(host='localhost',user='root',passwd='',db='snippets')
cursor =Con.cursor()
sql ="SELECT `language`,COUNT(*) as count FROM `end_snippet_cache` WHERE ide = '' AND `language` <> '' GROUP BY `language` ORDER BY count desc"
cursor.execute(sql)
result=cursor.fetchall()
for r in result:
	f.write(r[0]+" "+str(r[1])+"\n");
print "gist is over"

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

language_csdn = ['C++','Java','html','JavaScript','Python','PHP','C','css','SQL','Ruby','Go','C#']
language = ['C++','Java','HTML','JS','Python','PHP','C','CSS','SQL','Ruby','Go','C#']
language_gist = ['cpp','java','html','js','py','php','c','css','sql','rb','go','cs']
total_csdn = 0
total_gist = 0

fs = 24
z = [i*1000 for i in range(10)]
fy = 22

length = len(language_csdn)
data_gist = [0 for i in range(length)]


	
	
f = open('data/Language-Snippet-gist.txt','r')

for line in f:
	l = line.split(' ')
	if len(l)!=2:
		continue
	lang = l[0].strip(' \n')
	d = l[len(l)-1].strip(' \n')
	n = int(d)
	if cmp(lang,"html")==0 or cmp(lang,"xml")==0:
		data_gist[2] = n 
		total_gist += n
	else:
		for i in range(len(language_gist)):
			if cmp(lang,language_gist[i])==0:
				data_gist[i] = n
				total_gist += n
print "gist" + ": " + str(total_gist)
for t in data_gist:
	print t

data_csdn = [0 for i in range(length)]
f = open('data/Language-Snippet-csdn.txt','r')
for line in f:
	l = line.split(' ')
	if len(l)!=2:
		continue
	#print lang + " "+ d
	lang = l[0].strip(' ')
	d = l[len(l)-1].strip(' ')
	n = int(d)
	for i in range(len(language_csdn)):
		if cmp(lang,language_csdn[i])==0:
			data_csdn[i] = n
			total_csdn += n
print "csdn" + ": " + str(total_csdn)
for t in data_csdn:
	print t
percent_gist = []
percent_csdn = []
for i in range(length):
	percent_gist.append(data_gist[i]*1.0/total_gist)
	percent_csdn.append(data_csdn[i]*1.0/total_csdn)
for g in range(length):
	print str(percent_gist[g]) +" " + str(percent_csdn[g])


plt.figure(figsize = (14,7))  
ax = plt.subplot(111)   

mpl.rc('xtick', labelsize=10)
mpl.rc('ytick', labelsize=10)
#plt.grid(True)
ind = np.arange(len(data_csdn))+ 0 # the x locations for the groups
width = 0.4       # the width of the bars

rects1 = ax.bar(ind, percent_csdn, width, color='g')
rects2 = ax.bar(ind+width, percent_gist, width, color='y')


z = [i*10 for i in range(20)]
ax.set_ylabel('Language Ratio (%)',fontsize=fs)
ax.set_xlabel('Langugge',fontsize=fs) 
ax.set_ylim(0,0.5)               
ax.set_xticks(ind+width)
ax.set_xticklabels( language,rotation = 0,fontsize=fy )
ax.set_yticklabels(z,fontsize= fs )
plt.legend( (rects1[0], rects2[0]), ('code@CSDN','gist@GitHub'), loc='upper right',fontsize=fs)
def autolabel(rects):
    # attach some text labels
    for rect in rects:
        height = rect.get_height()
        ax.text(rect.get_x()+rect.get_width()/2., 1.01*height, '%.1f'%float(height*100),
                ha='center', va='bottom',size=20)
autolabel(rects1)
autolabel(rects2)

plt.show()
