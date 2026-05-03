# 项目指南 - NoteFlow Android 笔记应用

## 项目概述

NoteFlow 是一个简洁优雅的安卓记事本应用，采用现代 Android 开发技术构建。

### 技术栈
- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **数据库**: Room 2.6.1
- **UI**: Material Design 1.11.0
- **异步处理**: Kotlin Coroutines 1.7.3
- **构建工具**: Gradle 8.2
- **最低 SDK**: 24 (Android 7.0)
- **目标 SDK**: 34 (Android 14)

## 目录结构

```
├── app/                              # 主应用模块
│   ├── build.gradle                  # 应用级构建配置
│   ├── proguard-rules.pro            # ProGuard 规则
│   └── src/main/
│       ├── AndroidManifest.xml       # 应用清单
│       ├── java/com/noteflow/app/
│       │   ├── adapter/
│       │   │   └── NoteAdapter.kt        # 笔记列表适配器
│       │   ├── data/
│       │   │   ├── NoteDao.kt            # 数据访问对象
│       │   │   └── NoteDatabase.kt       # Room 数据库
│       │   ├── model/
│       │   │   └── Note.kt               # 笔记数据模型
│       │   ├── repository/
│       │   │   └── NoteRepository.kt     # 数据仓库
│       │   ├── ui/
│       │   │   ├── MainActivity.kt       # 主界面
│       │   │   └── NoteDetailActivity.kt # 笔记详情界面
│       │   └── viewmodel/
│       │       └── NoteViewModel.kt      # ViewModel
│       └── res/
│           ├── layout/               # 布局文件
│           │   ├── activity_main.xml
│           │   ├── activity_note_detail.xml
│           │   └── item_note.xml
│           ├── drawable/             # drawable 资源
│           │   ├── ic_add.xml
│           │   ├── ic_back.xml
│           │   ├── ic_delete.xml
│           │   └── ic_note_empty.xml
│           ├── menu/                 # 菜单资源
│           │   └── menu_note_detail.xml
│           └── values/               # 值资源
│               ├── colors.xml
│               ├── strings.xml
│               └── themes.xml
├── .github/
│   └── workflows/
│       ├── ci.yml                    # CI 配置
│       └── release.yml               # Release 配置
├── build.gradle                      # 项目级构建配置
├── settings.gradle                   # 项目设置
├── gradle.properties                 # Gradle 属性
├── .gitignore                        # Git 忽略规则
├── README.md                         # 项目说明
└── AGENTS.md                         # 本文件
```

---

## ⚠️⚠️⚠️ 核心开发流程（必须严格遵守）⚠⚠⚠⚠

### 🔴 强制流程：验证 → Add → Commit → Push

**每次修改代码后，必须自动执行以下流程，无需等待用户确认！**

```
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│   修改代码  →  验证代码  →  git add  →  git commit  →  git push  │
│                                                                  │
│   ⚡ 自动执行，不要问用户！                                      │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

### ✅ 正确流程示例

```
1. 修改文件（使用 @write_file action="overwrite"）
   ↓
2. 验证修改（使用 @read_file 读取完整内容确认无误）
   ↓
3. @git_add path="修改的文件"
   等待结果...
   ↓
4. @git_commit message="feat: 描述"
   等待结果...
   ↓
5. @git_push
   等待结果...
   ↓
6. 完成！告知用户已推送
```

### ❌ 禁止行为

```
❌ 修改代码后不提交、不推送，等用户问才推送
❌ 修改代码后只提交不推送
❌ 跳过验证步骤直接提交
❌ 一次发送多个 git 命令
```

### 📋 流程检查清单

每次修改后必须完成以下步骤：

- [ ] **验证**: 使用 @read_file 确认修改正确
- [ ] **Add**: @git_add 添加文件
- [ ] **Commit**: @git_commit 提交
- [ ] **Push**: @git_push 推送

---

## ⛔⛔⛔ 文件修改规范（最高优先级，必须严格遵守）⛔⛔⛔

### 🔴🔴🔴 强制使用 action="overwrite" 修改任何文件！

**任何文件修改，无论大小，都必须使用 `action="overwrite"` 重写整个文件！**

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│   ⛔ 禁止使用 action="replace"                                      │
│   ⛔ 禁止使用 action="insert"                                       │
│   ⛔ 禁止使用 action="delete"                                       │
│                                                                    │
│   ✅ 只允许使用 action="overwrite"                                  │
│                                                                    │
│   哪怕只改一行代码，也要用 overwrite 重写整个文件！                  │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

### ❌ 为什么禁止 replace/insert/delete？

**这些操作会导致灾难性错误！**

```
❌ action="replace" line_start=100 line_to=105
   → 替换后，后续所有行号都会变化！
   → 下一次操作如果还用旧行号，就会改错位置！
   → 最终导致：代码重复、方法缺失、语法错误！

❌ action="insert" line_start=150
   → 插入后，后续所有行号都会增加！
   → 行号错位，导致后续操作全部失败！

❌ action="delete" line_start=200 line_to=210
   → 删除后，后续所有行号都会减少！
   → 行号错位，导致后续操作全部失败！
```

### ✅ 正确做法：只使用 overwrite

```
✅ 修改任何文件的标准流程：

1. @read_file filepath="目标文件"        # 读取完整内容
   ↓
2. 在回复中编辑完整内容（修改需要改的部分）
   ↓
3. @write_file 
     filepath="目标文件" 
     action="overwrite"                 # ⚠️ 必须是 overwrite！
     content="完整修改后的文件内容"      # ⚠️ 必须是完整内容！
   ↓
4. @read_file filepath="目标文件"        # 验证修改结果
   ↓
5. @git_add → @git_commit → @git_push    # 提交推送
```

### 📋 overwrite 检查清单

每次使用 @write_file 时必须确认：

- [ ] **action**: 必须是 `"overwrite"`
- [ ] **content**: 必须是文件的**完整内容**
- [ ] **验证**: 修改后必须用 @read_file 验证

---

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

---

## 数据库结构

### Note Entity
```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
```

---

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

---

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

---

## 注意事项

1. **数据库迁移**: 修改 Entity 时需要编写 Migration
2. **生命周期感知**: 使用 `viewModelScope` 处理协程
3. **内存泄漏**: 避免在 ViewModel 中持有 Context
4. **UI 更新**: 只能在主线程更新 UI
5. **空值处理**: Kotlin 空安全，合理使用 `?.` 和 `!!`

---

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

---

## 联系方式

如有问题，请创建 Issue 或提交 Pull Request。

