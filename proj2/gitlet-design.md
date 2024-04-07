# Gitlet Design Document

**Name**: yoo2i

----------------------------------------------

## Classes and Data Structures

### Main

程序入口点；用来判断参数是否为空、是否合法、命令是否存在，并调用Repository的方法来实现功能。

#### Fields

无

---------------------------------------------

### Repository

用来实现本项目的所有命令，并提供一些辅助方法。

#### Fields

包含.gitlet文件夹中所有子文件夹和初始文件的file对象，均为静态成员变量。

------------

### Stage

用来维护暂存区映射关系，提供一系列封装好的方法供Repository调用。

#### Fields

- addition，Map类型，fileName -> fileHash，维护暂存区和实际序列化对象的映射关系；同时也只负责映射关系，暂存区实际的序列化对象文件由Repository中的对应功能提供。
- removal，Set类型，维护要取消跟踪的文件名称。

-------------------------------------

### Commit

代表版本类，实现了每个版本内容的存储。

#### Fields

- timeStamp，时间戳
- message，版本信息
- firstParent，提交树上的父commit
- secondParent，merge情况下的另一个父commit
- blobs，Map类型，fileName -> fileHash，维护本提交的各个文件版本信息。
- hash

--------------------------------------

### Branch

分支类，用来维护分支的操作。

#### Fields

无。

----------------------------

### Blob

代表被存储起来的历史文件版本，会序列化后出现在.gitlet/objects/blobs和.gitlet/objects/stage中。

#### Fields

- content，字节数组，用来保存文件内容。
- hash

--------------------------------------------

## Algorithms

### add

如果暂存区中该文件存在，则先把stage对象中的映射和.gitlet/objects/stage中的文件删除，然后判断上一个版本中是否存在相同内容的文件，不存在的话将该文件加入stage对象中的映射和.gitlet/objects/blobs。如果该文件被标记为待删除则取消待删除状态。最后将新文件和暂存区映射序列化。

### commit

从父commit中获得文件映射，然后根据暂存区修改映射（包括增加和减少），接着将文件从.gitlet/objects/stage移动到.gitlet/objects/blobs，并且清空暂存区的映射。最后移动头指针，**同时更新当前分支的头节点**，序列化暂存区和当前commit。

### rm

如果文件在暂存区，取消暂存。

如果文件被当前commit跟踪，标记为可删除，并且如果用户没删就删了。

如果没被当前跟踪就不删。

判断暂存区是否存在该文件，存在则取消暂存；然后判断当前文件是否被跟踪，如果是则标记为可删除并且删除（如果存在）。

### status

- Branches

​	遍历.gitlet/ref文件夹获得所有分支名的列表并对其排序，然后从HEAD中读出当前分支的索引，开始输出，如果是当前分支就加*

- Staged Files

​	stageArea中的addition的key， 排序后输出。

- Removed Files

​	stageArea中的removal，排序后输出。

### checkout

分为两个函数来实现，一个操作的是文件一个操作的是分支。

- 文件

​	将文件从指定commit中读出来（commit的id可能不是完整40位，需要支持），如果存在就在cwd中覆盖它，如果不存在就创建然后写入。

- 分支

  找到目标分支的头节点（目标commit），对于它跟踪的文件如果cwd中已经存在就覆盖，不存在就

创建然后写入；对于当前commit跟踪但是目标commit不跟踪的文件进行删除；清空暂存区（除非当前=目标）；切换到的分支被认为是当前分支。

​	找到当前分支的头节点和目标分支的头节点，如果cwd中有未被当前commit跟踪并且被目标commit跟踪且文件版本不同（会导致覆盖）的文件，退出；当前commit跟踪但是目标commit不跟踪的文件进行删除；目标commit跟踪的文件放入cwd（创建or覆盖）；清空暂存区并保存；调整HEAD指针；调整current_branch。

### reset

会同时移动head指针和分支维护的头指针，不会出现头分类状态（见文档最开始）。

找到当前节点和目标节点，如果cwd中有未被当前commit跟踪并且被目标commit跟踪且文件版本不同（会导致覆盖）的文件，退出；当前commit跟踪但是目标commit不跟踪的文件进行删除；目标commit跟踪的文件放入cwd（创建or覆盖）；清空暂存区并保存；调整当前分支头节点内容和HEAD内容。

## Persistence

## some problems

1.sha1无法调用

它只接受字符串or字节数组，其他对象需要转化为字节数组。

2.安排hash方便后面编码

3.尽量封装方便未来代码复用

4.真实.git/object将40位的哈希值拆成2+38来组织文件夹是为了方便查询，因为支持只输入前几个字符来确定commit，所以可能会出现只给前六位哈希值等情况，真实.git的组织方式能直接查到，而直接存储的会需要遍历一遍（该proj不在这上面卡时间所以我没加

5.空字符串不要设成null

```java
String result = "";
```

