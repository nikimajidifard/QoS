# QoS
## soheil Project
### Group Members : Niki Majidifar, Fatima Mirjalili , Banafsheh Gholinezhad.
    This program measures qos of eitaa app base on 4 main parameters latency, packet loss, download and upload rate

```

 ## project_ description
This program calculates the Quality of Experience (QoE) parameters for a specific application(Eitta). These parameters include ping test, download rate, upload rate, latency, and packet loss. Each parameter is represented as an item on the app's main page. When a user interacts with the corresponding button, the app sends a request to the Eitta API to calculate the related parameter. Once the calculation is completed, the app displays the calculated value and assesses the quality state, which can range from "bad" to "excellent." Additionally, the app assigns an order from 1 to 5 to provide a comprehensive explanation of the measured parameter's quality.

To achieve this functionality, the app consists of three main files: MainActivity.kt, list_item.xml, and activity_main.xml. 
In the MainActivity.kt file, the main logic for the list view is implemented. It initializes a list of items and sets up an adapter that connects the list to the list view. This allows the items to be displayed and interacted with in the app's user interface.
The list_item.xml file defines the layout for each item in the list view. It specifies how each item should be visually displayed, including aspects such as text color, size, and additional views if needed.
The activity_main.xml file defines the overall layout of the app's main activity. It contains a ListView widget, which serves as the container for displaying the list of items on the screen.
When the app is launched, the MainActivity.kt file is executed, initializing the list of items. The adapter is then set up to convert each item in the list into a corresponding view in the list view. Finally, the activity_main.xml file ensures that the ListView widget is appropriately positioned within the app's user interface.
Overall, this app provides users with insights into the performance and quality of a specific application by calculating the QoE parameters. It utilizes a list view to present these parameters as items on the main page, allowing users to interact with the app and view the calculated values.

```
## qos_فارسی
  به مجموعه ای از پارامتر هایی اشاره دارد که که در شبکه های کامپیوتری برای کنترل کیفیت خدمات استفاده می شودqos 
  هدف اصلی در واقع ارائه خدمات بهتر برای برنامه ها و سرویس شبکه است
  ما در این پروژه کیفیت سنجی پیامرسان ایتا را انجام داده ایم. 
  که به مدت زمان ارسال یک درخواست تا زمانی که پاسخ آن دریافت شود، اشاره دارد. latency پینگ یا ،  qos در پارامتر های قابل سنجش 
  ،است. از دست دادن بسته های داده زمانی اتفاق می افتد packet losssپارامتر تاثیر گذار بعدی که به محاسبه آن پرداختیم، 
  که  پهنای باند لینک ها کاملا اشغال شده باشد و روتر ها و سوییچ ها شروع به دراپ کردن بسته کرده باشند.
  ما در این پروژه برای محاسبه این پارامتر تعداد بالایی ریکوئست زدیم و سپس تعداد پکت های دریافت شده را از کل پکت های ارسالی 
  استpacket loss کم کردیم و در نهایت تقسیم بر کل پکت های ارسالی کردیم که مساوی با مقدار 
  اندازه گیری کردیم، نرخ دانلود است.به این صورت که ابتدا یک ریکوئست qos پارامتر بعدی که در محاسبه 
  برای دانلود یک فایل از ایتا زده شده و سپس با تایمر مدت زمانیکه طول می کشد این فایل دانلود شود، اندازه گیری شده
  سپس حجم فایل مورد نظر بر مدت زمان تقسیم شده که درواقع همان نرخ دانلود می باشد
  پارامتر بعدی نیز که محاسبه شده نرخ اپلود است. برای نرخ اپلود نیز با اپلود فایل و اندازه گیری فایل اپلود شده تقسیم بر زمان اپلود نرخ اپلود را به دست می اوریم. نتیحه به دست اوردن مقادیر پارامتر ها در فولدر result_log  ذخیزه شده است. 
  در نهایت با کمک مقادیر به دست امده ، مقدار qos  را حساب مبکنبم و برای هر پارامتر یک وزن در نظر میگیریم. در نهایت با توجه به مقدار این وزن ، مقدار Qoe  را در 5 دسته تقسیم کرده و نمایش می دهیم.  
