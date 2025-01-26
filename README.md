# 임베디드시스템 기말과제: 은행 창구 번호표 시스템 구현 (2023.12)

## 프로젝트 개요
이 프로젝트는 라즈베리파이를 사용하여 은행 창구 번호표 시스템을 구현한 것입니다. 고객이 번호표를 뽑고, 직원이 다음 고객을 부르는 과정을 LED와 LCD를 통해 시각적으로 표현합니다.

## 하드웨어 구현
- **Raspberry Pi 4**
- **LCD 모니터**, **RGB LED**, **two buttons**

### 회로도 및 연결
- **LED**: GND, R(GPIO 0), G(GPIO 2), B(GPIO 3)
- **LCD**: GND, 5V, I2C SDA(GPIO 8), I2C SCL(GPIO 9)
- **BUTTON 1 (고객)**: GND, 5V, 220Ω 저항, GPIO 28
- **BUTTON 2 (직원)**: GND, 5V, 220Ω 저항, GPIO 29
  <img width="949" alt="image" src="https://github.com/user-attachments/assets/c7c44f04-2b13-4688-8154-4d3888e024a9" />


### 동작 설명
1. **고객이 번호표를 뽑을 때**:
   - 버튼 1을 누르면 GPIO 28번에 Input이 됩니다.
   - GPIO 0, 2, 3번을 통해 파란 빛이 켜집니다.
   - LCD 모니터에 "Welcome Number" + N을 출력합니다.

2. **직원이 다음 고객을 부를 때**:
   - 버튼 2를 누르면 GPIO 29번에 Input이 됩니다.
   - GPIO 0, 2, 3번을 통해 초록 빛이 켜집니다.
   - LCD 모니터에 "Next Welcome" + N을 출력합니다.

3. **버튼이 눌리지 않았을 때**:
   - LCD 모니터에 "Current Number" + N을 출력합니다.

## 소프트웨어 구현
### 주요 클래스 및 변수
- **Bank.java**: 은행 창구 번호표 시스템을 구현한 메인 클래스입니다.
  - `waiting[]`: 1부터 50까지의 번호를 저장하는 배열입니다.
  - `customerCnt`: 대기 중인 고객의 수를 저장합니다.
  - `current`: 직원이 현재 업무를 보고 있는 고객의 번호를 저장합니다.

### 주요 기능
1. **초기화**:
   - `customerCnt`와 `current`를 0으로 초기화합니다.
   - `waiting` 배열을 1부터 50까지의 숫자로 초기화합니다.

2. **버튼 입력 처리**:
   - 고객 버튼이 눌리면 `customerCnt`가 증가하고, 파란 LED가 켜지며 LCD에 "Welcome Number" + N을 출력합니다.
   - 직원 버튼이 눌리면 초록 LED가 켜지며 LCD에 "Next Welcome" + N을 출력하고, `current`가 1 증가합니다.

3. **LCD 출력**:
   - 버튼이 눌리지 않았을 때는 LCD에 "Current Number" + N을 출력합니다.
