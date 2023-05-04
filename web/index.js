function test1() {
    let url = "device_file_servlet_action?action=get_infos";
    let data = {};
    data.feature_type = "BaiduPolygon";
    $.post(url,data,function(json){
        let list = [];
        list = json.aaData;
        for(let i=0;i<list.length;i++){
            console.log(list[i].feature_content);
        }
    })
}
function test2(){
    let url = "device_file_servlet_action?action=add_infos";
    let data={};
    data.feature_type = "BaiduPolygon";
    data.feature_content = "test";
    $.post(url,data,function(json){
        console.log("add success");
    })
}