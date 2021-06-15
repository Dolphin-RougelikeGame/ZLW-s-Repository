generate the obstacles in the rooms
===================================
使用了FMY的代码作为基础，主要增加内容为RoomRemodel类，作用为根据房间大小加入不同的障碍物，房间类型分为普通房、Boss房、宝箱房以及商店（未实现）。
FMY的代码被注释掉一部分（Fightdelegage类最后），使得敌我坦克未能出现。稍作修改即可当做游戏主角和地方小怪使用。
另外FMY的房间创建算法出现变化，RoomRemodel类相关方法所需参数需要随之变动

