import {component as content} from '@o2oa/oovm';
import {lp, o2} from '@o2oa/component';
import { isPositiveInt } from '../../../utils/common';
import { getPublicData, attendanceWorkPlaceV2Action } from '../../../utils/actions';
import template from './temp.html';
import oInput from '../../../components/o-input';
import oTimePicker from '../../../components/o-time-picker';
import oTimeMinutesSelector from '../../../components/o-time-minutes-selector';
import { setJSONValue } from '../../../utils/common';


export default content({
    template,
    components: {oInput, oTimePicker, oTimeMinutesSelector},
    autoUpdate: true,
    bind(){
        return {
            lp,
            fTitle: lp.workAddressAdd,
            form: {
              placeName: "",
              errorRange: "200",
              longitude: "",
              latitude: "",
              description: "",
            },
        };
    },
    afterRender() {
        this.loadBDMap();
    },
    // 加载地图api等资源
    async loadBDMap() {
        this.iconUrl = "../x_component_AttendanceV2/$Main/default/us_mk_icon.png"; //图标样式，发布时候修改为绝对路径
        const bdKey = await getPublicData("baiduAccountKey");
        const accountkey = bdKey || "Qac4WmBvHXiC87z3HjtRrbotCE3sC9Zg";
        const apiPath = "//api.map.baidu.com/getscript?v=2.0&ak="+accountkey+"&s=1&services=";
        if( !window.BDMapV2ApiLoaded ){
            o2.load(apiPath, () => {
                this._initBDMap();
            });
        } else {
            this._initBDMap();
        }
    },
    // 初始化地图
    _initBDMap() {
        console.debug("地图加载API加载完成，开始载入地图！");
        window.BDMapV2ApiLoaded = true;
        if (navigator.geolocation){
            try{
                navigator.geolocation.getCurrentPosition(this.initBDMap.bind(this), this.initBDMap.bind(this));
            }catch( e ){
                console.error(e);
                this.initBDMap();
            }
        }else{
            this.initBDMap();
        }
    },
     // 初始化地图
    initBDMap(position) {
        console.debug("位置信息", position);
        this.mapNode = this.dom.querySelector(".bd-map");
        if (this.mapNode) {
            this.createMap(position);
            this.addControls();
            this.addMapClick();
        } else {
            console.error("map node 不存在？？？");
        }
    },
    // 创建地图ui
    createMap( position ){
        let point = null;
        // if (position && position.coords){
        //     point = new BMap.Point(position.coords.longitude, position.coords.latitude);
        // }
        if( !point ){
            if( this.markerData && this.markerData.length > 0){
                let json = this.markerData[0];
                point = new BMap.Point(json.longitude, json.latitude);
            }else{
                point = new BMap.Point(120.135431, 30.27412);
            }
        }
        console.debug(point);
        this.map = new BMap.Map(this.mapNode);    // 创建Map实例
        this.map.centerAndZoom(point, 15);  // 初始化地图,设置中心点坐标和地图级别
        this.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    },
    // 添加地图控件
    addControls(){
        //向地图中添加缩放控件
        var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_RIGHT,type:BMAP_NAVIGATION_CONTROL_LARGE});
        this.map.addControl(ctrl_nav);
        // 添加地区选择器
        this.map.addControl(new BMap.CityListControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            offset: new BMap.Size(10, 20),
            // 切换城市之间事件
             onChangeBefore: function( ){
             },
            // 切换城市之后事件
             onChangeAfter:function( ){
             }
        }));
    },
    addMapClick() {
        this.map.addEventListener('click', (e)=> {
            console.debug('点击的经纬度：' , e);
            this.addMarkPoint( new BMap.Point(e.point.lng, e.point.lat));
            this.bind.form.longitude = e.point.lng;
            this.bind.form.latitude = e.point.lat;
        });
    },
    // 添加位置点
    addMarkPoint(point, placeName) {
        this.map.clearOverlays(); // 先清除
        const mIcon = new BMap.Icon(this.iconUrl, new BMap.Size(23, 25), {anchor: new BMap.Size(9, 25), imageOffset: new BMap.Size(-46, -21)});
        const marker = new BMap.Marker(point,{
            icon: mIcon,
            enableDragging : false
        });
        if (placeName) {
            const label = new BMap.Label(placeName, {"offset":new BMap.Size(0,-20)});
            marker.setLabel(label);
        } 
        this.map.addOverlay(marker);
    },
    close() {
        this.$parent.closeAddForm();
    },
    async submitAdd() {
        let myForm = this.bind.form;
        if (!myForm.placeName || myForm.placeName === '' || myForm.placeName.replace(/(^s*)|(s*$)/g, "").length == 0 ) {
            o2.api.page.notice(lp.workAddressForm.titleNotEmpty, 'error');
            return ;
        }
        if (!myForm.errorRange || myForm.errorRange === '' || myForm.errorRange.replace(/(^s*)|(s*$)/g, "").length == 0 ) {
            o2.api.page.notice(lp.workAddressForm.rangeNotEmpty, 'error');
            return ;
        }
        if (!isPositiveInt(myForm.errorRange)) {
            o2.api.page.notice(lp.workAddressForm.rangeNeedNumber, 'error');
            return ;
        }
        const json = await attendanceWorkPlaceV2Action("post", myForm);
        console.debug('新增成功', json);
        o2.api.page.notice(lp.workAddressForm.success, 'success');
        this.close();
    },
   
});
