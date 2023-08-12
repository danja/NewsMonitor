# from https://medium.com/@danmichaelo/vacuuming-fuseki-1c4ec11384a5

curl https://fuseki.hyperdata.it/$/datasets

curl -X POST https://fuseki.hyperdata.it/$/backup/newsmonitor
{
  "taskId" : "1" ,
  "requestId" : 37
}