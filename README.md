# ExpandableText
包含两种可展开的TextView
## 继承自TextView 
通过点击TextView实现展开收起
### 优点 
  可以实现html5上的那种效果,即在textView尾部添加Span实现点击
### 缺点
  不能实现复杂的效果,如不能设置展开收起的按钮的样式等
  
-----------------
  
## 继承自RelativeLayout
自定义组合控件的方法,通过点击自定义样式的button实现展开收起
### 优点
  可以自定义按钮的样式,可以实现渐影效果等等，可定制化比较强
### 缺点
  html5的那种效果难以做到
