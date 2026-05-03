# NoteFlow - 安卓记事本应用

一个简洁、优雅的安卓记事本应用，采用现代Android开发技术构建。

## 功能特性

- ✨ 创建、编辑、删除笔记
- 📝 支持标题和内容编辑
- 🔍 笔记搜索功能（即将推出）
- 📱 Material Design 设计风格
- 💾 本地数据持久化（Room数据库）
- 🎨 简洁直观的用户界面
- 📋 笔记列表和详情视图
- 🕒 自动保存时间戳

## 技术栈

- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **数据库**: Room
- **UI**: Material Design Components
- **异步处理**: Kotlin Coroutines
- **生命周期感知**: Android Jetpack Components

## 项目结构

```
noteflow-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/noteflow/app/
│   │   │   ├── adapter/          # RecyclerView适配器
│   │   │   ├── data/             # 数据库相关
│   │   │   ├── model/            # 数据模型
│   │   │   ├── repository/       # 数据仓库
│   │   │   ├── ui/               # 用户界面
│   │   │   └── viewmodel/        # ViewModel
│   │   ├── res/                  # 资源文件
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── gradle/
├── .github/workflows/            # GitHub Actions
├── build.gradle
└── settings.gradle
```

## 开发环境要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.2

## 快速开始

1. **克隆项目**
   ```bash
   git clone <your-repo-url>
   cd noteflow-app
   ```

2. **在Android Studio中打开**
   - 打开Android Studio
   - 选择 "Open an existing project"
   - 选择项目目录

3. **构建项目**
   ```bash
   ./gradlew build
   ```

4. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击运行按钮或执行：
   ```bash
   ./gradlew installDebug
   ```

## 构建变体

- **Debug**: 调试版本，包含调试信息
- **Release**: 发布版本，经过ProGuard优化

## CI/CD

项目使用GitHub Actions进行持续集成：

- **CI**: 每次推送到main/develop分支或PR时运行测试和构建
- **Release**: 创建发布版本时自动生成APK并发布到GitHub Releases

## 测试

运行单元测试：
```bash
./gradlew test
```

运行Android仪器测试：
```bash
./gradlew connectedAndroidTest
```

## 代码规范

- 遵循Kotlin编码规范
- 使用ktlint进行代码格式化
- 通过lint检查确保代码质量

## 待办事项

- [ ] 添加笔记分类/标签功能
- [ ] 实现笔记搜索
- [ ] 支持Markdown格式
- [ ] 添加笔记导出功能
- [ ] 实现云同步
- [ ] 支持深色模式
- [ ] 添加笔记提醒功能

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

如有问题或建议，请通过以下方式联系：

- 创建 Issue
- 发送邮件至 [your-email@example.com]

## 致谢

- Android Jetpack 团队
- Material Design 团队
- Kotlin 团队
