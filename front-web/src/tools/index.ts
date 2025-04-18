import {useClipboard} from "@vueuse/core";
import {ElMessage, ElMessageBox} from "element-plus";
import {post} from '@/net'

function fitByUnit(value:number, unit:string): string {
    const units = ['B','KB','MB','GB','TB','PB'];
    let index:number = unit.indexOf(unit);
    while( ((value < 1 && value !== 0) || value >= 1024) && (index >= 0 || index < units.length)) {
        if(value >= 1024 && index <= units.length - 1) {
            value = value / 1024;
            index ++;
        } else {
            value = value * 1024;
            index --;
        }
    }
    if(index < 0) return `0 B`;
    return `${value?.toFixed(2)} ${units[index]}`;
}

function percentageToStatus(percentage:number):string {
    if(percentage < 50) return 'success';
    else if(percentage < 80) return 'warning';
    else return 'exception';
}

function osNameToIcon(name:string):any {
    if(!name) return {icon: 'fa-linux', color: 'gray'};  // 处理undefined或null的情况

    if(name.indexOf('Ubuntu') >= 0) return {icon: 'fa-ubuntu', color: '#db4c1a'};
    else if(name.indexOf('CentOS') >= 0) return {icon: 'fa-centos', color: '#9dcd30'};
    else if(name.indexOf('macOS') >= 0) return {icon: 'fa-apple', color: 'gray'};
    else if(name.indexOf('Windows') >= 0) return {icon: 'fa-windows', color: '#3578b9'};
    else if(name.indexOf('Debian') >= 0) return {icon: 'fa-debian', color: '#a80836'};
    else return {icon: 'fa-linux', color: 'gray'};
}

function cpuNameToImage(name:string):string {
    if(name.indexOf('Apple') >= 0) return 'cpu-icons/Apple.png';
    else if(name.indexOf('AMD') >= 0) return 'cpu-icons/AMD.png';
    else return 'cpu-icons/Intel.png';
}

function rename(id:number, name:string, after:Function):void {
    ElMessageBox.prompt(
        '请输入新的服务器名称', '修改名称', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            inputValue: name,
            inputPattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{1,18}$/,
            inputErrorMessage: '名称只能包含中英文字符、数字、下划线',
        }).then(({value}) => post('/api/monitor/rename', // FIXME: 链接
        {id: id, name: value}, () => {
            ElMessage.success('主机更名成功');
            after();
        })
    )
}

const {copy} = useClipboard();
const copyIp = (ip:string) => copy(ip).then(() => ElMessage.success('成功复制IP到粘贴版'))

export {fitByUnit,percentageToStatus,osNameToIcon,cpuNameToImage,copyIp,rename}