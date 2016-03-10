RESOURCE:=src/main/resources
COMPILE_PATH:=target
CLASSES_PATH:=target/classes
TARGET_PATH:=/data/snsspider/qqspider


all:before package setting cp

clean:
	mvn clean

before:
	@mkdir -p $(TARGET_PATH) $(TARGET_PATH)/config

package:
	mvn package

setting:
	@mv $(RESOURCE)/*.* $(TARGET_PATH)/config/

cp:
	chmod +x spiderctl
	@cp -Rf spiderctl $(COMPILE_PATH)/*.jar $(COMPILE_PATH)/lib $(TARGET_PATH)/
