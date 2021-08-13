//定义一个写日志的函数 -- 往 <pre> 标签中加入内容
//定义一个函数并立即执行
//log 就是 return 的那个函数
const log = (function (){
    const pre = document.querySelector("pre");

    function f(n){
        if (n >= 0 && n < 10){
            return "0" + n;
        } else {
            return n;
        }
    }
    
    //编辑日期的
    function formatDate(date) {
        let year = date.getFullYear()
        let month = date.getMonth()
        let day = date.getDate()
        let hour = date.getHours()
        let minute = date.getMinutes()
        let second = date.getSeconds()

        return `${year}-${f(month)}-${f(day)} ${f(hour)}:${f(minute)}:${f(second)}`
    }

    //log 是这个函数
    return function (message){
        let now = new Date();
        let line = `[${now}] ${message}\r\n`
        pre.innerText += line
    }
})();

//由于代码结构略复杂，为了方便取到需要的全局变量
//把所有的全局变量保存在这里
const global = {}

//定义需要的全局变量
function defGlobalVars() {
    //来自 HTML 元素
    global.authorizeBtn = document.querySelector("#authorizeBtn");
    global.startBtn = document.querySelector("#startBtn");
    global.stopBtn = document.querySelector("#stopBtn");

    //为了进行声音的录制
    global.mediaRecoder = null;
}

//会在 mediaRecoder.start() 之后调用
//数据有效后定期执行该函数
//其中 event.data 中 是本次的有效数据
function onDataAvailable(event) {

}

//会在 mediaRecoder.stop() 之后调用
function onStop() {

}

//用户同意授权
function onAgree(stream) {
    alert("用户同意");

    global.mediaRecoder = new MediaRecoder(stream)
    global.mediaRecoder.ondataavailable = onDataAvailable;
    global.mediaRecoder.onstop = onStop;
}

//发生错误(不同意授权或者 onAgree 时候的错误)
function onError(error) {
    alert(error);
}

//请求用户授权信息
function authorize() {
    const constraints = {
        audio = true;
    }

    if(!navigator) {
        alert("浏览器不支持");
        return;
    }

    if(!navigator.mediaDevices) {
        alert("浏览器不支持");
        return;
    }

    if(!navigator.mediaDevices.getUserMedia) {
        alert("浏览器不支持");
        return;
    }

    navigator.mediaDevices.getUserMedia(constraints)
    .then(onAgree)
    .catch(onError)
}

//开始录制
function start() {
    //每秒钟数据有效一次 -- onDataAvailable 会执行一次
    global.mediaRecoder.start(1000);
}

function stop() {
    global.mediaRecoder.stop();
}

function bindButtonsEventListener() {
    global.authorizeBtn.addEventListener("click",authorize);
    global.startBtn.addEventListener("click",start);
    global.stopBtn.addEventListener("click", stop);
}

function main() {
    //执行全局变量的定义
    log("定义全局变量")
    defGlobalVars();

    //执行按钮事件的绑定
    log("进行事件绑定")
    bindButtonsEventListener();
}

window.addEventListener("load", main);