# Taboo Game - 害你在心口难开

![image](https://github.com/user-attachments/assets/b335b66a-8005-4f93-bf18-d3a9ad960f3d)
![image](https://github.com/user-attachments/assets/6c2db62c-7a9b-4b69-8ecf-89274c54ff13)
![image](https://github.com/user-attachments/assets/e2387f48-47e0-427c-b699-289b269127ac)
![image](https://github.com/user-attachments/assets/f4380f28-a056-4eac-9a31-b676a32c359a)





## 项目简介

Taboo Game（害你在心口难开）是一款有趣的多人互动游戏，基于WebSocket实现实时交互。每个玩家头顶会显示一张只有其他玩家可见的卡牌，玩家需要通过诱导他人做出卡牌上描述的行为来获得胜利。

## 技术栈

### 后端

- WebSocket (实时通信)
- Redis (定时任务和发布订阅机制)
- Spring Boot (Java后端框架)
- Token认证机制

### 前端

- HTML5/CSS3/JavaScript
- WebSocket客户端
- 响应式设计 (适配移动端)

## 功能特性

1. **实时玩家列表**：显示所有在线玩家（除自己外）的名称、词牌和死亡次数
2. **词牌管理**：
   - 随机分配初始词牌
   - 点击"换牌"按钮可更换词牌
   - 换牌时显示上一个词牌内容
3. **玩家状态管理**：
   - 记录玩家死亡次数
   - 处理移动端熄屏导致的连接中断
   - 基于Redis的定时清理机制
4. **断线重连**：
   - 熄屏后重新连接会更换词牌
   - 使用Token维持会话状态
