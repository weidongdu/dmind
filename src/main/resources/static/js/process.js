const wsUrl = "ws://localhost:8091";//localhost 根据实际情况替换成 响应的ip 或者域名
let ws = new WebSocket(wsUrl);

ws.onopen = function (evt) {
    console.log("Connection open ...");
    // ws.send("Hello WebSockets!");
};

ws.onmessage = function (evt) {
    // ws.close();
    let msgObj = JSON.parse(evt.data);
    if ('process' === msgObj.key) {
        //设置 进度条
        document.querySelector("#iProgress").value = msgObj.value;
    }
};

ws.onclose = function (evt) {
    console.log("Connection closed.");
};


function notify(content) {
    let div = document.createElement("div");
    div.setAttribute("class", "notification is-danger is-light");
    div.setAttribute("onclick", "deleteNotify()");
    div.innerText = content;
    document.querySelector("#params").append(div);
}


function deleteNotify() {
    let notificationList = document.querySelectorAll('.notification');
    if (notificationList !== null && notificationList.length > 0) {
        for (let i = notificationList.length - 1; i >= 0; i--) {
            notificationList[0].parentNode.removeChild(notificationList[i]);
        }
    }
}


function parseAdd() {
    let obj = getDmindObj();
    if (obj.count === 0) {
        postJson("/parse/add", obj, parseAddRes);
        btnParse('#iBtnSubmit')
    }
}

function parseTop() {
    let obj = getDmindObj();
    if (obj.count === 0) {

        postJson("/parse/top", obj, parseTopRes);
        btnParse('#iBtnTop')
    }
}

function btnParse(cs) {
    let btn = document.querySelector(cs);
    btn.setAttribute("class", "button is-fullwidth is-info is-loading");
    const interval = setInterval(function () {
        btn.setAttribute("class", "button is-fullwidth is-info");
        clearInterval(interval);
    }, 5000);

    if (ws.readyState === ws.CLOSED) {
        ws = new WebSocket(wsUrl)
    }
}

function getDmindObj() {
    let count = 0;
    let obj = {};
    //校验 url
    let params = document.querySelector("#params");
    let fileUrl = params.querySelector("input[name=url]").value;
    if (fileUrl === '' || fileUrl === undefined) {
        notify("上传文件不能为空")
        count += 1;
    } else {
        obj.url = fileUrl;
    }

    let baiduLacServer = params.querySelector("input[name=baiduLacServer]").value;
    if (baiduLacServer === '' || baiduLacServer === undefined) {
        notify("分词接口不能为空")
        count += 1;
    } else {
        obj.baiduLacServer = baiduLacServer;
    }

    let topicWord = params.querySelector("input[name=topicWord]").value;
    if (topicWord === '' || topicWord === undefined) {
        notify("核心词(Keyword)")
        count += 1;
    } else {
        obj.topicWord = topicWord;
    }

    count += checkTopN(params.querySelector("input[name=l1]"), 'l1', obj);
    count += checkTopN(params.querySelector("input[name=l2]"), 'l2', obj);
    count += checkTopN(params.querySelector("input[name=l3]"), 'l3', obj);
    count += checkTopN(params.querySelector("input[name=l4]"), 'l4', obj);

    let mailTo = params.querySelector("input[name=mailTo]").value;
    if (mailTo === '' || mailTo === undefined) {
        notify("收件地址Email不能为空")
        count += 1;
    } else {
        obj.mailTo = mailTo;
    }

    obj.isTopic = document.querySelector('input[name="isTopic"]:checked').value
    obj.divided = document.querySelector('input[name="divided"]:checked').value
    obj.stopWords = params.querySelector("textarea").value;
    obj.stopSymbol = params.querySelector("input[name=stopSymbol]").value;

    console.log(JSON.stringify(obj))
    obj.count = count;
    return obj;
}

function checkTopN(ele, index, obj) {
    if (ele.value === '' || ele.value === undefined || parseInt(ele.value) > parseInt(ele.max) || parseInt(ele.value) < 1) {
        notify(index + "级topN 最大" + ele.max + " 最小1");
        return 1;
    } else {
        obj[index] = ele.value;
        return 0;
    }
}

function postJson(url, obj, parse) {
    fetch(url, {
        method: 'post',
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(obj)
    })
        .then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => {
            parse(response)
        });
}

function parseTopRes(response) {
    console.log(JSON.stringify(response))
    if (response !== null && response.code === 0) {
        let s = ",";
        document.querySelector("textarea").value = '';
        document.querySelector("input[name=stopSymbol]").value = s;
        document.querySelector('#iBtnTop').setAttribute("class", "button is-fullwidth is-info");

        response.data.forEach(item => {
            document.querySelector("textarea").value += item + s;
        });
    }
}

function parseAddRes(response) {
    console.log(JSON.stringify(response))
}

//获取 oss list
function parseList() {
    let obj = {};
    obj.url = document.querySelector("#oss-pre").value;
    postJson("/parse/list", obj, parseOssListRes);
}

function parseOssListRes(response) {
    console.log(response);
    if (response !== null && response !== undefined && response.code === 0) {
        //打开模态框
        document.querySelector('#iModal').setAttribute("class","modal is-active");
        let ossList = document.querySelector('#ossList');
        //删除子节点
        let children = ossList.children;
        for(let i = children.length - 1; i >= 0; i--) {
            ossList.removeChild(children[i]);
        }

        response.data.forEach(item =>{
            let tr = document.createElement('tr');
            ossList.append(tr);

            let t_btn = document.createElement('td');
            let btn = document.createElement('button');
            btn.setAttribute("class","button");
            btn.setAttribute("onclick","selectOssFileName(this)");
            btn.innerText = "选择";
            tr.append(t_btn);t_btn.append(btn);

            let t_key = document.createElement('td');
            t_key.innerText = item.key;
            tr.append(t_key);

            let t_size = document.createElement('td');
            t_size.innerText = item.size;
            tr.append(t_size);

            let t_lastModified = document.createElement('td');
            t_lastModified.innerText = formatTime(new Date(item.lastModified));
            tr.append(t_lastModified);

        })

    }
}

function closeModal(){
    document.querySelector('#iModal').setAttribute("class","modal");
}
function selectOssFileName(ele){
    let url = ele.parentNode.parentNode.children[1].textContent;
    let params = document.querySelector("#params");
    params.querySelector("input[name=url]").value = url;
}

function formatTime(date){
    let y = date.getFullYear()
    let m = date.getMonth() + 1
    m = m < 10 ? '0' + m : m
    let d = date.getDate()
    d = d < 10 ? '0' + d : d
    let h = date.getHours()
    h = h < 10 ? '0' + h : h
    let minute = date.getMinutes()
    minute = minute < 10 ? '0' + minute : minute
    let second = date.getSeconds()
    second = second < 10 ? '0' + second : second
    return y +'-'+ m +'-'+ d + ' ' + h + ':' + minute + ':' + second
}