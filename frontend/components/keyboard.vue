<template>
  <view class="page">
    <!-- 车牌号码输入框 -->
    <view class="carNumber">
      <view class="weui-cells__title">请输入车牌号码后在使用</view>
      <!-- 车牌号头两位 -->
      <view class="carNumber-items">
        <view class="carNumber-items-box" @click="openKeyboard">
          <view class="carNumber-items-province carNumber-items-box-list">{{carnum[0] || '豫'}}</view>
          <view class="carNumber-items-En carNumber-items-box-list">{{carnum[1] || 'A'}}</view>
        </view>
        <!-- 常规 -->
        <view v-for="(item, index) in carnum.slice(2)" :key="index" class="carNumber-item" @click="openKeyboard">{{item || ''}}</view>
        <!-- 新能源 -->
        <view class="carNumber-item" :class="{ 'carNumber-item-newpower': !showNewPower }">
          <view v-if="!showNewPower" @click="showPowerBtn">
            <view class="carNumber-newpower-add">+</view>
            <view>新能源</view>
          </view>
          <view v-else @click="openKeyboard">{{carnum[7]}}</view>
        </view>
      </view>
    </view>

    <!-- 提交车牌 -->
    <button class="carNumberBtn" @click="submitNumber" style="background: #333333;color:#fff;" type="default">确定</button>

    <!-- 虚拟键盘 -->
    <view class="keyboard" v-if="KeyboardState">
      <view class="keyboardClose">
        <view class="keyboardClose_btn" @click="closeKeyboard">关闭</view>
      </view>
      <!-- 省份简写键盘 -->
      <view v-if="!carnum[0]" class="keyboard-item">
        <view v-for="(line, index) in provinces" :key="index" class="keyboard-line">
          <view v-for="item in line" :key="item" class="keyboard-btn" @click="bindChoose(item)">{{item}}</view>
        </view>
        <view class="keyboard-del" @click="bindDelChoose">
          <text class="font_family icon-shanchu keyboard-del-font"></text>
        </view>
      </view>
      <!-- 车牌号码选择键盘 -->
      <view v-if="carnum[0]" class="keyboard-item iscarnumber">
        <view v-for="(line, index) in numbers" :key="index" class="keyboard-line">
          <view v-for="item in line" :key="item" class="keyboard-btn" @click="bindChoose(item)">{{item}}</view>
        </view>
        <view class="keyboard-del" @click="bindDelChoose">
          <text class="font_family icon-shanchu keyboard-del-font"></text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      provinces: [
        ['京', '沪', '粤', '津', '冀', '晋', '蒙', '辽', '吉', '黑'],
        ['苏', '浙', '皖', '闽', '赣', '鲁', '豫', '鄂', '湘'],
        ['桂', '琼', '渝', '川', '贵', '云', '藏'],
        ['陕', '甘', '青', '宁', '新'],
      ],
      numbers: [
        ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
        ["A", "B", "C", "D", "E", "F", "G", "H", "J", "K"],
        ["L", "M", "N", "P", "Q", "R", "S", "T", "U", "V"],
        ["W", "X", "Y", "Z", "港", "澳", "学"]
      ],
      carnum: [],
      showNewPower: false,
      KeyboardState: true
    };
  },
  methods: {
    bindChoose(item) {
      if (!this.carnum[6] || this.showNewPower) {
        this.carnum.push(item);
        this.$set(this, 'carnum', this.carnum);
      }
    },
    bindDelChoose() {
      if (this.carnum.length != 0) {
        this.carnum.pop();
        this.$set(this, 'carnum', this.carnum);
      }
    },
    showPowerBtn() {
      this.showNewPower = true;
      this.KeyboardState = true;
    },
    closeKeyboard() {
      this.KeyboardState = false;
    },
    openKeyboard() {
      this.KeyboardState = true;
    },
    submitNumber() {
      if (this.carnum[6]) {
        // 跳转到tabbar页面
      }
    }
  }
};
</script>

<style>
.page{
  background: #fff;
  position: absolute;
  top:0;
  bottom:0;
  width: 100%;
}
.weui-cells__title{
  margin-top:.77em;
margin-bottom:.3em;
padding-left:15px;
padding-right:15px;
color:#999;
font-size:14px;

}
/* 虚拟键盘 */
.keyboard{
  height: auto;
  background: #d1d5d9;
  position: fixed;
  bottom:0;
  width: 100%;
  left:0;
}
.keyboard-item{
  padding:10rpx 0 5rpx 0;
  position: relative;
  display: block;
}
/* 关闭虚拟键盘 */
.keyboardClose{
  height: 70rpx;
  background-color: #f7f7f7;
  overflow: hidden;
}
.keyboardClose_btn{
  float: right;
  line-height: 70rpx;
  font-size: 15px;
  padding-right: 30rpx;
}
/* 虚拟键盘-省缩写 */

/* 虚拟键盘-行 */
.keyboard-line{
  margin:0 auto;
  text-align: center;
}
.iscarnumber .keyboard-line{
  text-align: left;
  margin-left: 5rpx;
}
/* 虚拟键盘-单个按钮 */
.keyboard-btn{
  font-size: 17px;
  color: #333333;
  background: #fff;
  display: inline-block;
  padding:18rpx 0;
  width: 63rpx;
  text-align: center;
  box-shadow: 0 2rpx 0 0 #999999;
  border-radius: 10rpx;
  margin:5rpx 6rpx;
}
/* 虚拟键盘-删除按钮 */
.keyboard-del{
  font-size: 17px;
  color: #333333;
  background: #A7B0BC;
  display: inline-block;
  padding:4rpx 55rpx;
  box-shadow: 0 2rpx 0 0 #999999;
  border-radius: 10rpx;
  margin:5rpx;
  position: absolute;
  bottom:5rpx;
  right: 6rpx;
}
.keyboard-del-font{
  font-size:25px;
}

/* 车牌号码 */
.carNumber-items{
  text-align: center;
}
.carNumber-items-box{
  width: 158rpx;
  height: 90rpx;
  border: 2rpx solid #CCCCCC;
  border-radius: 4rpx;
  display: inline-block;
  vertical-align: middle;
  position: relative;
  margin-right: 30rpx;
}
.carNumber-items-box-list{
  width: 76rpx;
  height: 70rpx;
  line-height: 70rpx;
  text-align: center;
  display: inline-block;
  font-size: 18px;
  margin:10rpx 0;
  vertical-align: middle;
}
.carNumber-items-province{
  border-right: 1rpx solid #ccc;
}
.carNumber-items-box::after{
  content: "";
  width: 6rpx;
  height: 6rpx;
  position: absolute;
  right: -22rpx;
  top: 40rpx;
  border-radius: 50%;
  background-color: #ccc;
}
.carNumber-item{
  width: 76rpx;
  height: 90rpx;
  font-size: 18px;
  text-align: center;
  border: 2rpx solid #CCCCCC;
  border-radius: 4rpx;
  line-height: 90rpx;
  display: inline-block;
  margin:0 4rpx;
  vertical-align: middle;
}
/* 新能源 */
.carNumber-item-newpower{
  border: 2rpx dashed #A8BFF3;
  background-color: #F6F9FF;
  color: #A8BFF3;
  font-size: 12px;
  line-height: 45rpx;
}
.carNumber-newpower-add{
  font-size: 18px;
}

/* 确认按钮 */
.carNumberBtn{
  border-radius: 4rpx;
  margin:80rpx 40rpx;
}
</style>
