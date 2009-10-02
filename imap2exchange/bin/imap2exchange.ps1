# set them if you want, or use these hacks to find them
#################################################
$EXCHANGE_CONVERSION_HOME=(Get-Item (Get-Location)).FullName
Set-Item Env:EXCHANGE_CONVERSION_HOME $EXCHANGE_CONVERSION_HOME

echo "====================================================="
echo EXCHANGE_CONVERSION_HOME=$EXCHANGE_CONVERSION_HOME
echo "====================================================="

# Name the main class
#################################################
$CLASSNAME="edu.jmu.email.conversion.jmu.JmuExchangeConversionCmdLineUtil"

# Set the Classpath
#################################################
$cp=".\;"
dir "$($EXCHANGE_CONVERSION_HOME)\lib" | foreach { $cp="$($cp)$($_.FullName);" }
$cp="$($cp)$($EXCHANGE_CONVERSION_HOME)\config"

# Set Java Args
#################################################
if ($OPTS) { Remove-Variable OPTS }
$OPTS= '"-Xms2g" "-Xmx2g" "-Xmn1g" "-Xss128k" '
$OPTS+='"-XX:ParallelGCThreads=20" "-XX:+UseConcMarkSweepGC" "-XX:+UseParNewGC" '
$OPTS+='"-XX:SurvivorRatio=8" "-XX:TargetSurvivorRatio=90" "-XX:MaxTenuringThreshold=31" '
$OPTS+="`"-Dlog4j.configuration=file:///$($EXCHANGE_CONVERSION_HOME)\config\log4j.properties`" "
$OPTS+="`"-DEXCHANGE_CONVERSION_HOME=$($EXCHANGE_CONVERSION_HOME)`" "
#$OPTS+='-Dhttp.auth.preference="basic" '

java.exe $OPTS -cp $cp $CLASSNAME $args
