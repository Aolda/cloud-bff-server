# AoldaCloud BFF Server

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)]()

## Table of Contents

- [Overview](#overview)
- [Purpose](#purpose)
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [License](#license)
- [Contact](#contact)

## Overview

**AoldaCloud BFF API**는 React 웹 애플리케이션과 AoldaCloud OpenStack API 사이에서 **Backend for Frontend (BFF)** 역할을 수행하는 중간 서버입니다. 이 서버는 클라이언트 애플리케이션의 요구에 맞춰 데이터를 집계하고 가공하여 전달하며, 클라이언트와 백엔드 시스템 간의 복잡성을 줄이고 성능을 최적화하는 데 중점을 둡니다.

## Purpose

이 프로젝트의 주요 목적은 리액트 웹 애플리케이션이 AoldaCloud OpenStack API와 상호 작용할 때 필요한 모든 백엔드 로직을 제공하는 것입니다. BFF 서버는 다음과 같은 역할을 합니다:

- 클라이언트의 요청을 처리하고, 필요한 데이터를 OpenStack API로부터 가져와 적절히 변환 및 통합.
- 여러 API 호출을 단일 호출로 집계하여 클라이언트의 성능 최적화.
- 사용자 인증 및 권한 관리를 중앙에서 처리하여 보안을 강화.
- 클라이언트와 백엔드 사이의 네트워크 트래픽을 최소화하고, 데이터의 일관성을 유지.

## Features

- **통합 데이터 집계**: 클라이언트의 요구에 맞춘 맞춤형 API 응답을 생성하기 위해 여러 OpenStack API 호출을 집계.
- **보안 관리**: JWT 기반의 인증 시스템을 사용하여 안전한 사용자 인증 및 세션 관리.
- **성능 최적화**: 클라이언트 애플리케이션의 성능을 높이기 위해 단일 API 호출로 여러 데이터를 통합하여 반환.
- **확장성**: 추가적인 기능 확장이 용이하도록 설계된 모듈형 아키텍처.

## Architecture

아래 다이어그램은 AoldaCloud BFF API의 아키텍처를 시각적으로 나타냅니다. BFF 서버는 리액트 웹 애플리케이션과 AoldaCloud의 OpenStack API 사이에서 데이터를 중계하고 처리하는 역할을 합니다.
```plain
+-----------------------------+
|        React Frontend        |
+-------------+---------------+
              |
              v
+-------------+---------------+
|      AoldaCloud BFF Server   |
|  (Data Aggregation, Security)|
+-------------+---------------+
              |
              v
+-------------+---------------+
|     AoldaCloud OpenStack     |
|            API               |
+-----------------------------+
```

## Installation

### Prerequisites

- Java 21 이상
- Gradle
- Docker (선택 사항)

### Build and Run

프로젝트를 빌드하고 실행하려면, 자바와 메이븐 환경이 필요합니다. 프로젝트를 클론한 후 Gradle 명령어를 통해 빌드하고 실행할 수 있습니다.

## Usage

애플리케이션이 실행된 후, 제공된 API 엔드포인트를 통해 BFF API에 접근할 수 있습니다. 이를 통해 리액트 웹 애플리케이션은 AoldaCloud OpenStack API와 안전하게 상호 작용할 수 있습니다.

## API Documentation

프로젝트에 포함된 Swagger UI를 통해 API 문서를 자동 생성하고 확인할 수 있습니다. 이를 통해 제공되는 모든 API 엔드포인트에 대한 상세한 정보를 얻을 수 있습니다.
* Swagger UI: http://localhost:8080/swagger-ui/index.html

## License

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하십시오.

## Contact

프로젝트와 관련된 문의 사항이나 기여 제안이 있으시면, 이메일을 통해 연락해 주십시오.

- Email: [minshigee@gmail.com](mailto:minshigee@gmail.com)
