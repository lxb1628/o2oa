import {component as content} from '@o2oa/oovm';
import {lp} from '@o2oa/component';
import template from './template.html';

export default content({
    template,
    autoUpdate: true,
    bind() {
        return {
            lp,
        };
    },
    afterRender() {
        const loadingBar = this.dom.querySelector(".o2-mask-loadingBar");
        const size = this.dom.getSize();
        let tmpLeft = (size.x-120)/2;
        if( tmpLeft < 0 ) {
            tmpLeft = 0;
        }
        let tmpTop = (size.y-30)/2;
        if (tmpTop<=0) {
            tmpTop = (window.screen.height-30)/2-100;
        }
        loadingBar.setStyle("left", ""+tmpLeft+"px");
        loadingBar.setStyle("top", ""+tmpTop+"px");
    }
     
});
