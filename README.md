Тестовое задание для Yandex мобилизации 2017
===

Задача
---
Согласно минимальным требованиям было необходимо реализовать следующую **`минимальную функциональность`**:

	1.Поле для ввода текста, который будет переведён на другой язык; 
	2.Переключатель языка и варианты перевода, которые появляются, когда пользователь вводит текст в поле.
	3.Возможность добавить переведённое слово или предложение в избранное.
	4.Возможность просмотра истории переводов.
	5.Возможность просмотра избранного.
	6.Перевод с одного языка на другой с помощью API Яндекс.Переводчика.

Также для упрощения разработки был представлен следующий пример пользовательского интерфейса:

Перевод|История|Избранное
:--------:|:-------:|:-----:
<img src="https://goo.gl/dyiwzV" width="300" height="500">|<img src="https://goo.gl/mCcF9t" width="300" height="500">|<img src="https://goo.gl/mFXuwZ" width="300" height="500">

Было реализовано
---
**За время работы над проектом были реализованы следующие возможности:**
1. Возможность переводить на языки поддерживаемые [yandex transalte api](https://tech.yandex.com/translate/)
2. Возможность просмотра словарных статей при помощи [yandex dictionary api](https://tech.yandex.ru/dictionary/)
3. Возможность сохранения в избранное
4. Возможность просмотра истории переводов
5. Возможность просмотра словарынх статей уже переведенных слов
6. min api 15

Стек технологий использованных в проекте
---
1. [RxJava](https://github.com/ReactiveX/RxJava/tree/1.x)
2. [Retrofit](http://square.github.io/retrofit/)
3. [Retrolambda]

Пример работы программы
---
|SpashScreen|Окно перевода|Пример перевода|
|:--------:|:-------:|:-----:|
<img src="https://cloud.githubusercontent.com/assets/22330346/25656347/4d7e47b4-3001-11e7-9036-3cdbfd8fdb2f.jpg" width="300" height="500">|<img src="https://cloud.githubusercontent.com/assets/22330346/25656424/a11da3ba-3001-11e7-9a5f-29a2eab0c1bf.jpg" width="300" height="500">|<img src="https://cloud.githubusercontent.com/assets/22330346/25656369/5fc52690-3001-11e7-986e-e60858ffe7f7.jpg" width="300" height="500">|

|История|Избранное|Ошибка|
|:--------:|:-------:|:-----:|
<img src="https://cloud.githubusercontent.com/assets/22330346/25656485/e67fc6d6-3001-11e7-92ec-82fdff0088c2.jpg" width="300" height="500">|<img src="https://cloud.githubusercontent.com/assets/22330346/25656349/4f853e64-3001-11e7-9c8e-03a42c3de4e6.jpg" width="300" height="500">|<img src="https://cloud.githubusercontent.com/assets/22330346/25656354/556f9cac-3001-11e7-9115-fc816abd145e.jpg" width="300" height="500">|

