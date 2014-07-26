JVE - это  простой редактор анимации. Файл видеопроекта имеет TeX-подобный синтаксис. Поддерживаютися пользовательские функции, вставка javascript-кода. Есть система сетевой сборки видео: вы можете собирать одно и то же видео на нескольких компьютерах одновременно (каждый компьютер будет собирать свою часть видео, затем главный из них объединит части и предоставит цельное видео).

Синтаксис файла видеопроекта:

####Шапка. 

`\frameSize{1920, 1080}` Задаём размер кадра

`\frameRatio{25}` Задаём количество кадров в секунду

`\tempDir{/home/nameless/Desktop/temp/}` Устанавливаем временную папку, куда будут складываться собранные кадры перед сборкой видеофайла. Эта папка будет очищаться каждый раз перед сборкой видео, поэтому не храните в ней свои файлы.

`\audio{/home/nameless/Projects/full.mp3}` Не обязательно - задаём фоновый звук видео

`\addFilePath{/home/nameless/Projects/lines/}` Задаём папку для поиска (чтобы можно указывать не полный путь к файлам)

####Блок video
*Внутри блока video располагаются сцены*

    \begin{video}
        \begin{scene} ... \end{scene}
        \begin{scene} ... \end{scene}
    \end{video}

####Блок scene
    \begin{scene}
        \duration{10}% Задаём длительность сцены в секундах
        \name{Intro}% Не обязательно - задаём имя сцены
        %Затем рисуем объекты на кадре
        \drawRectangle{0, 0, 1920, 1080, 255, 255, 255, 255}
        \drawString{Java Video Editor, 300, 300+quadParabolicFadeOut*300, DejaVu Sans Light, 140, 35, 35, 35, 255*normalTime}
    \end{scene}
        
**normalTime** внутри сцены изменяется от 0 (первый кадр сцены) до 1 (последний кадр сцены).

**absoluteTime** равен времени от начала сцены в секундах.

####Блок layer
*Внутри блока сцены вы можете создать блок слоя. Это удобно, если вы хотите применить какой-нибудь эффект только к части уже нарисованного изображения.*
    
    \begin{scene} 
        \drawRectangle{0, 0, 1920, 1080, 255, 255, 255, 255}
        \drawString{Java Video Editor, 300, 300+quadParabolicFadeOut*300, DejaVu Sans Light, 140, 35, 35, 35, 255*normalTime}
        \begin{layer}
            \drawString{text on layer, 300, 600+quadParabolicFadeOut*300, DejaVu Sans Light, 140, 35, 35, 35, 255*normalTime}
            \blur{5}% Применяем размывание только для слоя.
        \end{layer}
    \end{scene}
*Внутри каждого слоя может располагаться сколько угодно вложенных слоёв*

####Вставка JS
Если вы вставляете JS-код вне блока сцены, то он выполняется только при старте сборки кадров. Так, например, можно вставлять функции:

    \injectJS{function tripleBezierCurve(t, from, to) {
        t1=(1-t);
        t1_2=(1-t)*t1;
        t_2=t*t;
        return from*t1*t1_2+3*from*t*t1_2+3*to*t_2*t1+to*t_2*t;
    }}

Функцию не нужно объявлять каждый раз при отрисовке очередного кадра, она будет существовать на протяжении всей сборки.    
Если вы вставляете JS-код внутри блока сцены, то он будет выполняться каждый раз при сборке кадра. Так можно задавать переменные:

    \injectJS{h=400-tripleBezierCurve(normalTime, 200, 0)}
    \drawString{Text 1, 80, h, Calibri, 85, 32, 32, 32, 255*(1-parabolicFadeIn)}
    \drawString{Text 2, 80, h+90}
    \drawString{Text 3, 80, h+170, Calibri, 40}

####Пользовательские функции (макросы)
Вы можете один раз задать некую последовательность команд и затем вызывать её по имени:

*Один раз вне блока сцены*

    \begin{macros}
        \name{lines}
        \params{h1, h2, h3, h4, h5, h6}
        \drawImage{1.png, 0, h1, 320, 4000}
        \drawImage{2.png, 320, h2}
        \drawImage{3.png, 640, h3}
        \drawImage{4.png, 960, h4}
        \drawImage{5.png, 1280, h5}
        \drawImage{6.png, 1600, h6}
    \end{macros}

*Внутри сцены*

`\useMacros{lines, -3184, -3184, -3184, -3184, -3184, -3184}`

*Внутри другой сцены*

    \useMacros{lines,
    tripleBezierCurve(normalise(normalTime, 0, 0.3), -1200, -3184),
    tripleBezierCurve(normalise(normalTime, 0.05, 0.35), -1200, -3184),
    tripleBezierCurve(normalise(normalTime, 0.1, 0.4), -1200, -3184),
    tripleBezierCurve(normalise(normalTime, 0.15, 0.45), -1200, -3184),
    tripleBezierCurve(normalise(normalTime, 0.2, 0.50), -1200, -3184),
    tripleBezierCurve(normalise(normalTime, 0.25, 0.55), -1200, -3184)}

####Укороченная запись команды
Если вы внутри сцены уже вызвали команду с определёнными параметрами:

`\drawImage{1.png, 0, h1, 320, 4000}`

То следующую, у которой несколько параметров с конца подряд совпадают с указанными в прошлый раз, можно сократить до изменившихся параметров:

`\drawImage{2.png, 320, h2}`

Работает только для команд рисовки (не для эффектов)
           
####Комментарии
Всё, что следует после знака % - комментарий:
`\drawImage{2.png, 320, h2}%всё, что после знака, игнорируется`

####Встроенные функции:
sin, cos, tan, sqrt, max, min, abs, pow.

Также существуют автозамены:        

    parabolicFadeIn -> pow(normalTime, 2))
    parabolicFadeOut -> pow(normalTime-1, 2))
    quadParabolicFadeIn -> pow(normalTime, 4))
    quadParabolicFadeOut -> pow(normalTime-1, 4))
    sineFadeIn -> sin(normalTime*1.57))
    sineFadeOut -> sin(normalTime/1.57+1.57))
    fullSineFadeIn -> (sin(normalTime*3.14-1.57)/2+0.5))
    fullSineFadeOut -> (sin(normalTime*3.14+1.57)/2+0.5))
    circleFadeIn -> sqrt(2*normalTime-pow(normalTime,2)))
    circleFadeOut -> sqrt(1-pow(normalTime,2))

####Вставка
Вы можете вставить код любого другого .tex-файла в данный командой `\import{URL}`

####Полный список команд и их параметров:

`drawRectangle(x, y, width, height, red, green, blue, alpha)` рисует прямоугольник.

`drawString(text, x, y, font name, font size, red, green, blue, alpha, centering)` рисует строку.
*centering: left, center, right, left-limited, center-limited, right-limited*

Если центровка "-limited", то параметр "font size" означает ширину, в которую будет вписан текст

`drawEllipse(x, y, width, height, red, green, blue, alpha)` рисует эллипс.

`drawImage{URL, x, y, width, height}` рисует изображение.

`fillImage{URL, deltaX, deltaY, width, height}` заполняет плиткой из изображения.

`blur{raduis}` размывает.

`circuit{}` выделяет края.

`negate{chanels}` инвертирует цвет.

f.e.: `negate{green, alpha}`

`multiply{number, chanels}` умножает цвет на значение.
f.e.: `negate{0.5, alpha}` делает слой полупрозрачным

`add{number, chanels}` добавляет к цвету значение.

`setColor{number, chanels}` устанавливает цвет.

`scale{scaleX, scaleY}` масштабирует.

`move{deltaX, deltaY}` перемещает.

`rotate{angle, x, y}` вращает вокруг точки

`clear{x, y, width, height}` очищает область.

Пример в файле example.tex

Использование: первый параметр метода Main.main - адрес .tex-файла