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

从父commit中获得文件映射，然后根据暂存区修改映射（包括增加和减少），接着将文件从.gitlet/objects/stage移动到.gitlet/objects/blobs，并且清空暂存区的映射。最后移动头指针，序列化暂存区和当前commit。

### rm

如果文件在暂存区，取消暂存。

如果文件被当前commit跟踪，标记为可删除，并且如果用户没删就删了。

如果没被当前跟踪就不删。

判断暂存区是否存在该文件，存在则取消暂存；然后判断当前文件是否被跟踪，如果是则标记为可删除并且删除（如果存在）。

## Persistence

