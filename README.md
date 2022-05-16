# کامپایلر زبان جایتون <br>
پروژه درس اصول طراحی کامپایلر <br>

---
### مقدمه
هدف از این پروژه طراحی کامپایلر زبان جایتون می باشد. طراحی این کامپایلر به صورت فاز به فاز
پیش خواهد رفت بنابراین فاز های بعدی ادامه همین قسمت خواهند بود. سند زبان جایتون در فایل
ضمیمه در اختیار شما قرار گرفته است. در این فاز از شما انتظار می رود پس از مطالعه سند این
زبان و آشنایی با قواعد آن، برای یک ورودی که قطعه کدی به زبان جایتون است خروجی مورد نظر
که توضیحات آن در ادامه است را تولید نمایید. فاز یکم پروژه صرفا جهت آشنایی شما با قواعد زبان
جایتون و ابزار ANTLR و فراگیری چگونگی خروجی گرفتن از توابع طراحی شده است و بسیار ساده
می باشد .
### توضیحات
با توجه به ویدیویی که در اختیارتان قرار داده شده است به راه اندازی اولیه پروژه بپردازید. در این
ویدئو چگونگی عملکرد گرامر ها و طرز کار با listener ها نیز توضیح داده شده است. <br>
با توجه به ویدئو شما باید پس از ایمپورت کردن یک قطعه کد جایتون، با استفاده از Listener ها یک
خروجی تولید نمایید. این خروجی نمایگر اجزای مختلف قطعه کد ورودی و جزئیات آن است. <br>
مواردی که داخل [ ] قرار ندارند نشان دهنده اجزای مختلف یک برنامه در حالت کلی می باشد (کلاس، اینترفیس، متغیر و...) و باید عینا در خروجی نوشته شوند. موارد داخل [ ] وابسته به قطعه کد ورودی می باشد و در واقع توضیحی برای هر جزء هستند (نام کلاس ها، نام اینترفیس ها، نام متغیرها، نوع متغیر ها و...) که باید توسط شما با توجه به قطعه کد ورودی تکمیل شوند. <br>
شکل کلی خروجی مورد نظر به صورت زیر است: <br>
```
program start {
    [program body]
}    

import class: [class name]

class: [class name]/ class parents: ([parent name], )*{
    [class body]
}

class constructor: [constructor name]{
    parameters list: [ ([[parameter type] [parameter name]], )+])?
    [method body]
}

main method{
    parameters list: [ ([[parameter type] [parameter name]], )+])?
    [method body]
}

class method: [method name]/ return type=[return type]{
    parameters list: [ ([[parameter type] [parameter name]], )+])?
    [method body]
}

field: [field name]/ type=[type]

nested statement{
    
}
```
---
 **در ادامه یک نمونه ورودی و خروجی برای درک بهتر آورده شده است:**

```python
                                                    | program start{
import Nothing                                      |     import class: Nothing1
import Nothing2                                     |     import class: Nothing2
class Human(Nothing, Nothing2){                     |     class: Human/ class parents: object, {
    Nose nose                                       |         field: nose/ type= Nose
    Hand[2] hands                                   |         field: legs/ type= Leg
    Leg[2] legs                                     |         field: hands/ type= Hand
    int calories                                    |         field: calories/ type= int
    bool isHungry                                   |         field: isHungry/ type= bool
                                                    | 
    def Human(Nose n){                              |         class constructor: Human{
        self.nose = n                               |             parameter list: [Nose n]
    }                                               |         }
    def Voice speak(){                              |         class method: speak/ return type: Voice{
        Voic voice                                  |             field: voice/ type= Voice
        return voice                                | 
    }                                               |         }
    def void eat(Food food, int c){                 |         class method: eat{
        calories += c                               |             parameter list: [Food food, int c, ]
        newFood = food                              |
        while(self.isHungry){                       |
            Food newFood = Food()                   |
            eat(newFood)                            |
            self.isHungry = self.checkIsHungry()    |
        }                                           |
    }                                               |         }
    def bool checkIsHungry(){                       |         class method: checkIsHungry/ return type: bool{
        return true                                 |             
    }                                               |         }
}                                                   |     }
                                                    | }
```