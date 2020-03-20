#include <SoftwareSerial.h>
/**
 * Copyright 2020 © John Melody Melissa
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Author : John Melody Melissa
 * @Copyright: John Melody Melissa & Tan Sin Dee © Copyright 2020
 * @INPIREDBYGF: Cindy Tan <3
 * @Class: Bluetooth.ino
 */
SoftwareSerial bluetooth (2, 4);

unsigned long previousMillis = 0;
const long interval = 500;
static uint32_t tmp;

void setup() {
  pinMode(13, OUTPUT);
  bluetooth.begin(9600);
  delay(200);
  bluetooth.print("AT+NAME https://johnmelodyme.github.io/");
  delay(3000);
}

void loop() {
  if (bluetooth.available()){
    if (bluetooth.read() == '1') {
      digitalWrite(13, !digitalRead(13));
    }
  }
}