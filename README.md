# Taboo Game - 害你在心口难开

![image](https://github.com/user-attachments/assets/b335b66a-8005-4f93-bf18-d3a9ad960f3d)
![image](https://github.com/user-attachments/assets/6c2db62c-7a9b-4b69-8ecf-89274c54ff13)
![image](https://github.com/user-attachments/assets/e2387f48-47e0-427c-b699-289b269127ac)
![image](https://github.com/user-attachments/assets/f4380f28-a056-4eac-9a31-b676a32c359a)





## 项目简介

Taboo Game（害你在心口难开）是一款多人互动游戏，玩家需要诱导其他玩家做出他们头顶卡牌上写的动作或说出特定词语。本项目使用 WebSocket 实现实时交互，基于 SpringBoot 后端和 Vue 3 前端构建。

## 技术栈

### 前端

- Vue 3
- Element Plus
- JavaScript
- WebSocket

### 后端

- Spring Boot (Java 1.8)
- WebSocket
- Redis (用于 Token 管理和发布订阅)

## 功能特点

- 实时多人在线游戏体验
- 玩家状态实时同步
- 自动词牌更换机制
- 断线重连处理
- Token 无过期时间设计（通过Redis管理）

## 游戏规则

1. 每个玩家头顶会显示一个词牌（只有其他玩家可见）
2. 玩家需要诱导其他玩家做出或说出他们词牌上的内容
3. 当玩家做出自己词牌上的行为时，该玩家"死亡"
4. "死亡"玩家可以点击换牌按钮更换新词牌
5. 游戏没有明确的胜利条件，主要以娱乐互动为主
