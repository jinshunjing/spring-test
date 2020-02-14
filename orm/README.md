# orm
- MyBatis自动生成代码
- MongoDB/ElasticSearch Model

## 自动生成MyBatis代码
- 首先安装plugin到本地的Maven仓库：cd plugin, mvn install
- 执行修改数据库DDL
- 修改generatorConfig.xml要自动生成的表
- 执行下列命令
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

## 包结构
- dal: MyBatis自动生成代码，不要放自己写的代码，可能会被意外删除

