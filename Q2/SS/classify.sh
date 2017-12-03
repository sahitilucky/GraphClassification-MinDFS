javac -cp $CLASSPATH=:../GraphLib/Motif/ -target 1.7 -source 1.7  *.java
java -cp :../GraphLib/Motif/:../GraphLib/lib  DataSplitting $1 $2 $3 $4 

