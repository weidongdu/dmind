accessid = 
accesskey = 
host = 

g_dirname = '';
g_object_name = '';
g_object_name_type = '';
now = timestamp = Date.parse(new Date()) / 1000;

var policyText = {
    "expiration": "2021-12-01T12:00:00.000Z", //设置该Policy的失效时间，超过这个失效时间之后，就没有办法通过这个policy上传文件了
    "conditions": [
        ["content-length-range", 0, 1048576000] // 设置上传文件的大小限制
    ]
};

var policyBase64 = Base64.encode(JSON.stringify(policyText));
message = policyBase64;
var bytes = Crypto.HMAC(Crypto.SHA1, message, accesskey, {asBytes: true});
var signature = Crypto.util.bytesToBase64(bytes);

function check_object_radio() {
    var tt = document.getElementsByName('myradio');
    for (var i = 0; i < tt.length; i++) {
        if (tt[i].checked) {
            g_object_name_type = tt[i].value;
            break;
        }
    }
}

function get_dirname() {
    dir = document.getElementById("dirname").value;
    if (dir != '' && dir.indexOf('/') != dir.length - 1) {
        dir = dir + '/'
    }
    //alert(dir)
    g_dirname = dir
}


function get_suffix(filename) {
    pos = filename.lastIndexOf('.')
    suffix = ''
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

function get_fileName(filename) {
    pos = filename.lastIndexOf('.')
    if (pos != -1) {//有file type
        filename = filename.substring(0, pos)
    }
    return filename;
}

function calculate_object_name(filename) {
    // g_object_name += "${filename}"
    suffix = get_suffix(filename)
    filename_head = get_fileName(filename)
    g_object_name = g_dirname + filename_head + "_" + (new Date().valueOf()) + "_" + suffix;
    return ''
}

function get_uploaded_object_name(filename) {

    tmp_name = g_object_name
    tmp_name = tmp_name.replace("${filename}", filename);
    return tmp_name

}

function set_upload_param(up, filename, ret) {
    g_object_name = g_dirname;
    if (filename != '') {
        suffix = get_suffix(filename)
        calculate_object_name(filename)
    }
    new_multipart_params = {
        'key': g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid,
        'success_action_status': '200', //让服务端返回200,不然，默认会返回204
        'signature': signature,
    };

    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });

    up.start();
}

var uploader = new plupload.Uploader({
    runtimes: 'html5,flash,silverlight,html4',
    browse_button: 'selectfiles',
    //文件的最大上传大小,不写该参数则上传文件大小无限制
    container: document.getElementById('container'),
    flash_swf_url: 'lib/plupload-2.1.2/js/Moxie.swf',
    silverlight_xap_url: 'lib/plupload-2.1.2/js/Moxie.xap',
    url: 'http://oss.aliyuncs.com',
    filters:{
        mime_types : [ //只允许上传csv文件
            { title : "文本 files", extensions : "csv" }
        ],
        max_file_size : '50mb', //最大只能上传
        prevent_duplicates : true, //不允许选取重复文件
        multi_selection: false,
    },
    init: {
        PostInit: function () {
            document.getElementById('ossfile').innerHTML = '';
            document.getElementById('postfiles').onclick = function () {
                set_upload_param(uploader, '', false);
                return false;
            };
        },

        FilesAdded: function (up, files) {
            console.log(up.files.length)

            //设置单选 files为一个数组，里面的元素为本次添加到上传队列里的文件对象
            if (up.files.length > 1){
                up.splice(0, up.files.length-1)//保留最后一个
                let f = document.getElementById("ossfile");
                let childs = f.childNodes;
                for(let i = childs.length - 1; i >= 0; i--) {
                    f.removeChild(childs[i]);
                }

            }

            plupload.each(up.files, function (file) {
                document.getElementById('ossfile').innerHTML
                    += '<tr>' +
                    '<td id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')' +
                    '</td>' +
                    '<td>' +
                    '</td>' +
                    '<td>' +
                    '<div class="progress">' +
                    '</div>' +
                    '</td>' +
                    '</tr>';
            });
        },

        BeforeUpload: function (up, file) {
            check_object_radio();
            get_dirname();
            set_upload_param(up, file.name, true);
        },

        UploadProgress: function (up, file) {
            var d = document.getElementById(file.id).parentNode;
            var progBar = d.getElementsByTagName('td')[2].getElementsByTagName('div')[0];
            progBar.innerHTML = '<progress class="progress is-success is-small" max="100" value="' + file.percent + '">' + file.percent + "%</progress>";
        },

        FileUploaded: function (up, file, info) {
            if (info.status == 200) {
                document.getElementById(file.id).parentNode.getElementsByTagName('td')[1].innerHTML = get_uploaded_object_name(file.name);
                document.querySelector('input[name=url]').value = get_uploaded_object_name(file.name);
            } else {
                document.getElementById(file.id).parentNode.getElementsByTagName('td')[1].innerHTML = info.response;
            }
        },

        Error: function (up, err) {
            alert("\nError xml:" + err.response);
        }
    }
});

uploader.init();
