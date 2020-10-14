const wsUrl = "ws://localhost:8091";//localhost 根据实际情况替换成 响应的ip 或者域名
let ws = new WebSocket(wsUrl);

ws.onopen = function(evt) {
    console.log("Connection open ...");
    // ws.send("Hello WebSockets!");
};

ws.onmessage = function(evt) {
    // ws.close();
    let msgObj = JSON.parse(evt.data);
    if ('process' === msgObj.key){
        //设置 进度条
        document.querySelector("#iProgress").value = msgObj.value;
    }
};

ws.onclose = function(evt) {
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
    if (count === 0){
        console.log(ws.readyState)
        console.log(ws.CLOSED)
        postJson("/parse/add",obj);
        let btn = document.querySelector('#iBtnSubmit');
        btn.setAttribute("class", "button is-fullwidth is-info is-loading");
        const interval = setInterval(function () {
            btn.setAttribute("class", "button is-fullwidth is-info");
            clearInterval(interval);
        }, 1000);

        if(ws.readyState === ws.CLOSED){
            ws=new WebSocket(wsUrl)
        }
    }
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

function postJson(url, obj) {
    fetch(url, {
        method: 'post',
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(obj)
    }).then(res => console.log(res));
}