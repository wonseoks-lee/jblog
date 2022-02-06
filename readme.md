### 계정 생성
show databases;

use jblog;

create user 'jblog'@'%' identified by 'jblog';

grant all privileges on jblog.* to 'jblog'@'%';

flush privileges;