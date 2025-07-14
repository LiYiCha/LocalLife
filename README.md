# Local Life Service Platform

## Project Overview
An O2O life service platform based on Spring Cloud Alibaba, integrating local merchant resources to provide one-stop services for product transactions, community interaction, and promotional offers. The system processes over 100,000 orders daily, supports more than 5,000 TPS concurrent transactions, with main functional modules including:

- **Core Transaction Chain**: Product Search → Shopping Cart → Order Creation → Payment Settlement → Fulfillment Notification
- **Community Ecosystem**: User Posting/Commenting + Intelligent Recommendation + Content Moderation
- **Search Service**: Millisecond-level product/post search powered by Elasticsearch
- **High Availability Architecture**: Sentinel circuit breaking & degradation + Seata distributed transactions + Redis distributed locks

## Technical Architecture
![Architecture Diagram]

**Technology Matrix**:
| Domain           | Technology Selection                                                                   |
|------------------|----------------------------------------------------------------------------------------|
| Microservice Framework | Spring Cloud Alibaba 2023.0.1 + Spring Boot 3.3.5                                |
| Service Governance       | Nacos 2.2.3 (Service Registry & Configuration Center)                          |
| Traffic Control          | Sentinel 1.8.7 (Gateway QPS control + Hotspot parameter rate limiting)         |
| Distributed Transactions | Seata 1.8.1 (AT mode) + Redis 7.0 (Distributed lock implementation)             |
| Search Service           | Elasticsearch 8.12 (Product/Community content search) + IK Analyzer              |
| Data Storage             | MySQL 8.0 (Sharding) + Redis 7.0 (Cache & Distributed Locks) + MongoDB 6.0 (Logs) |
| Service Communication    | OpenFeign 3.4.1 + LoadBalancer 4.0.3 + RabbitMQ 3.12 (Asynchronous communication) |

## Core Features

### Microservice Modules

├── life-gateway # API Gateway (Dynamic routing / Rate limiting)

├── life-auth # Authentication Center (JWT)

├── order-service # Order Service (State machine / Distributed transactions)

├── member-service # Membership Service (Multi-role permission system)

├── product-service # Product Service (ES search / Inventory management)

├── search-service # Search Service (Elasticsearch engine)

├── community-service # Community Service (Content recommendation / Moderation)

└── coupon-service # Coupon Service (Rule engine / Anti-abuse)

### Search Service Implementation
Search feature highlights:

- Supports fuzzy search of product names/categories/tags
- Integrated with IK Chinese Analyzer + Pinyin search suggestions
- Highlights keywords in search results
- Millisecond-level response time (average 120ms)

### Project Highlights

**High-performance Search Service**

Built on Elasticsearch for product/community content search engine  
Search result response time ≤ 150ms (with 1 million+ data entries)  
Supports synonym expansion and search term correction

**Distributed Transaction Control**

Seata AT mode ensures consistency between order creation and inventory deduction  
Redis distributed lock implements concurrency control for payment interfaces  
Exception compensation mechanism (success rate 99.99%)

**Intelligent Traffic Management**

Gateway Layer: Sentinel cluster flow control (5000 QPS)  
Interface Level: Hotspot parameter rate limiting (100 QPS per product dimension)  
Thread Isolation: Bulkhead pattern protects core resources

O2O Local Life Service Platform (Spring Cloud Alibaba microservice architecture)  
Tech Stack: Spring Cloud Alibaba / Nacos / Sentinel / Seata / Elasticsearch / Redis / MySQL  
Project Responsibilities:

- Led the design of the microservice architecture, using Nacos as the service registry and configuration center, managed multi-environment configurations through bootstrap.yml, supporting collaborative operation of 5 microservices
- Built an Elasticsearch product search cluster, integrated IK Analyzer and Pinyin suggestion functionality (response time ≤ 150ms), achieved fuzzy search for product titles/categories, processing over 200,000 search requests daily
- Designed a distributed transaction solution using Seata AT mode to ensure data consistency between `order-service` and `product-service`, exception compensation mechanisms improved transaction success rate to 99.99%
- Implemented three-tier traffic protection using Sentinel, achieving 5000 QPS cluster flow control at the gateway layer and hotspot parameter rate limiting (100 QPS per product dimension), system availability increased from 99.3% to 99.95%
- Designed an anti-abuse coupon system (`coupon-service`) combining Lua scripts to implement atomic inventory operations, increasing malicious request interception rate by 65%
