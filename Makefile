CONFIG:=config
COMPILE_PATH:=target
CLASSES_PATH:=target/classes
TARGET_PATH:=~/data/snsspider/qqspider

BEFOREDEPLOY_CMD:="/bin/rm -f $(TARGET_PATH)/config/*.* $(TARGET_PATH)/lib/*.* $(TARGET_PATH)/*.* $(TARGET_PATH)/spiderctl"
CP_CMD1:="/bin/cp -Rf $(COMPILE_PATH)/lib/*.* $(TARGET_PATH)/lib"
CP_CMD2:="/bin/cp -Rf spiderctl $(COMPILE_PATH)/*.jar $(TARGET_PATH)/"

all:before package deploy

clean:
	@mvn clean

before:
	@mkdir -p $(TARGET_PATH) $(TARGET_PATH)/config $(TARGET_PATH)/lib

package:
	mvn package

beforedeploy:
	@(echo $(BEFOREDEPLOY_CMD)) 
	@(/bin/rm -f $(TARGET_PATH)/config/*.* $(TARGET_PATH)/lib/*.* $(TARGET_PATH)/*.* $(TARGET_PATH)/spiderctl)

deploy:beforedeploy setting cp

setting:
	@cp $(CONFIG)/*.template $(TARGET_PATH)/config/

cp:
	chmod +x spiderctl
	@(echo "$(CP_CMD1)") 
	@(/bin/cp -Rf $(COMPILE_PATH)/lib/*.* $(TARGET_PATH)/lib)
	@(echo "$(CP_CMD2)") 
	@(/bin/cp -Rf spiderctl $(COMPILE_PATH)/*.jar $(TARGET_PATH)/)
