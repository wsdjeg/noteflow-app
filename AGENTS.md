# NoteFlow - AI Agent 指南

## 项目概述

NoteFlow 是一个简洁优雅的安卓记事本应用，采用现代 Android 开发技术构建。

## 技术栈

- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **数据库**: Room
- **UI**: Material Design Components
- **异步处理**: Kotlin Coroutines
- **构建工具**: Gradle 8.2
- **最低SDK**: API 24 (Android 7.0)
- **目标SDK**: API 34 (Android 14)

## 项目结构

```
app/src/main/java/com/noteflow/app/
├── adapter/          # RecyclerView 适配器
├── data/             # 数据库相关 (Room Database, DAO, Entity)
├── model/            # 数据模型
├── repository/       # 数据仓库层
├── ui/               # Activity/Fragment
└── viewmodel/        # ViewModel
```

## 开发规范

### 代码风格
- 遵循 Kotlin 编码规范
- 使用有意义的变量和函数命名
- 添加必要的注释，特别是复杂逻辑
- 保持函数简短，单一职责

### 架构规范
- 遵循 MVVM 架构模式
- ViewModel 不应持有 Activity/Fragment 引用
- 使用 Repository 模式管理数据源
- 数据库操作使用 Coroutines 处理异步

### 命名规范
- **类名**: PascalCase (如 `NoteViewModel`)
- **函数名**: camelCase (如 `getNoteById`)
- **变量名**: camelCase (如 `noteTitle`)
- **常量**: UPPER_SNAKE_CASE (如 `DATABASE_NAME`)
- **布局文件**: snake_case (如 `activity_main.xml`)
- **资源ID**: snake_case (如 `tv_note_title`)

### 布局前缀规范
- `activity_` - Activity 布局
- `fragment_` - Fragment 布局
- `item_` - RecyclerView 列表项
- `dialog_` - 对话框布局
- `tv_` - TextView
- `iv_` - ImageView
- `rv_` - RecyclerView
- `btn_` - Button
- `et_` - EditText

## 常用命令

```bash
# 构建项目
./gradlew build

# 运行测试
./gradlew test

# 运行 Android 仪器测试
./gradlew connectedAndroidTest

# 清理构建
./gradlew clean

# 安装调试版
./gradlew installDebug

# 生成签名 APK
./gradlew assembleRelease
```

## 数据库结构

### Note Entity
- `id`: Long (主键, 自增)
- `title`: String (笔记标题)
- `content`: String (笔记内容)
- `createdAt`: Long (创建时间戳)
- `updatedAt`: Long (更新时间戳)

## 注意事项

1. **数据库迁移**: 修改 Entity 时需要编写 Migration
2. **生命周期感知**: 使用 `viewModelScope` 处理协程
3. **内存泄漏**: 避免在 ViewModel 中持有 Context
4. **UI 更新**: 只能在主线程更新 UI
5. **空值处理**: Kotlin 空安全，合理使用 `?.` 和 `!!`

## 功能模块

### 已实现
- [x] 笔记列表显示
- [x] 创建新笔记
- [x] 编辑笔记
- [x] 删除笔记
- [x] 自动保存时间戳

### 待实现
- [ ] 笔记搜索功能
- [ ] 笔记分类/标签
- [ ] Markdown 支持
- [ ] 笔记导出
- [ ] 云同步
- [ ] 深色模式
- [ ] 笔记提醒

## Git 提交规范

```
feat: 新功能
fix: 修复 bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 添加测试
chore: 构建/工具变动
```

## 联系方式

如有问题，请创建 Issue 或提交 Pull Request。

