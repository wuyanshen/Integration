Ext.ns('Ai.ws');
Ai.ws.MainPanel = Ext.extend(Ext.Panel, {
    layout: 'form',
    labelWidth: 200,
    labelAlign: 'right',
    buttonAlign: 'left',
    initComponent: function(){
        this.initWSAddress();
        this.initMethod();
        this.initPanel();
        this.initButton();
        this.initResultPanel();
        this.initExceptionPanel();
        
        this.initLayout();
        Ai.ws.MainPanel.superclass.initComponent.call(this);
    },
    
    initMethod: function(){
        this.method = new Ext.form.ComboBox({
            fieldLabel: WSTEST.method,
            name: 'method',
            triggerAction: 'all',
            forceSection: true,
            typeAhead: true,
            mode: 'local',
            displayField: 'name',
            valueField: 'name',
            store: new Ext.data.JsonStore({
                fields: ['name']
            }),
            ctCls: 'haha'
        });
        this.method.on('select', this.repaintParamPanel, this);
    },
    
    loadMethod: function(){
        var me = this;
        // 查询所有方法
        wstestFacade.getAllMethod(this.wsAddress.getValue(), {
            asycn: false,
            callback: function(data){
                if (data != null && data != '') {
                    data = eval('(' + data + ')');
                    me.method.store.loadData(data);
                    
                    // 如果加载接口方法成功，清空异常信息
                    me.exPanel.body.update('');
                }
            },
            exceptionHandler: function(message){
                me.exPanel.body.update(WSTEST.loadMethod).setStyle('color', 'red');
            }
        });
    },
    
    initPanel: function(){
        this.panel = new Ext.Panel({
            title: WSTEST.inParam,
            labelWidth: 200,
            layout: 'form',
            cls: 'param_input'
        });
    },
    
    initExceptionPanel: function(){
        this.exPanel = new Ext.Panel({
            title: WSTEST.exception,
            labelWidth: 200,
            layout: 'form',
            cls: 'param_input'
        });
    },
    
    initResultPanel: function(){
        this.resultPanel = new Ext.Panel({
            title: WSTEST.result,
            labelWidth: 200,
            layout: 'form',
            cls: 'param_input'
        });
    },
    
    repaintParamPanel: function(combo, r){
        // 通过选中的方法调用后台，返回jsonData，重绘参数面板
        this.panel.onDestroy();
        this.panel.items.clear();
		 // 清空输出结果
        this.resultPanel.onDestroy();
        // 清空异常信息
        this.exPanel.body.update('');
        if (!combo) {
            return;
        }
        // 方法名称
        var methodname = combo.getValue();
        // 选中方法后查询参数
        var self = this;
        wstestFacade.getParamByMethodNameAndWsUrl(this.wsAddress.getValue(), methodname, {
            asycn: false,
            callback: function(data){
                //组装参数
                this.jsonData = Ext.decode(data);
                for (var i = 0; i < this.jsonData.length; i++) {
                    self.panel.add(new Ai.ws.ParamPanel({
                        jsonData: this.jsonData[i]
                    }));
                }
                
                self.panel.doLayout();
            },
            exceptionHandler: function(message){
                parent.Ext.MessageBox.alert('', MSG.FAILURE +
                message);
            }
        });
    },
    
    initWSAddress: function(){
        this.wsAddress = new Ext.form.TextField({
            fieldLabel: WSTEST.address,
            value: 'http://10.10.16.181/easy-demo/ws/mutiModule_module1_module11_bmDomainService?wsdl',
            width: 500
        });
        this.wsAddress.on('blur', this.loadMethod, this);
    },
    
    initButton: function(){
        this.buttons = [{
            text: WSTEST.run,
            handler: this.doRun,
            scope: this
        }, {
            text: WSTEST.clear,
            handler: this.doClear,
            scope: this
        }];
    },
    
    doRun: function(){
        var webserviceUrl = this.wsAddress.getValue();
        var methodName = this.method.getValue();
        var result = [];
        var items = this.panel.items.items;
        for (var i = 0, len = items.length; i < len; i++) {
            result.push(items[i].getValue());
        }
        var paramStr = Ext.encode(result);
        var self = this;
        
        // 增加返回结果
        wstestFacade.executionMethod(webserviceUrl, methodName, paramStr, {
            asycn: false,
            callback: function(data){
            
                this.jsonData = Ext.decode(data);
                self.resultPanel.onDestroy();
                self.exPanel.body.update('');
                
                for (var i = 0; i < this.jsonData.length; i++) {
                    self.resultPanel.add(new Ai.ws.ReturnPanel({
                        jsonData: this.jsonData[i]
                    }));
                }
                self.resultPanel.doLayout();
            },
            exceptionHandler: function(message){
                self.exPanel.body.update(message).setStyle('color', 'red');
            }
        });
    },
    
    doClear: function(){
        // 清空输入参数
        this.panel.cascade(function(obj){
            if (obj.isXType('textfield')) {
                obj.setValue('');
            }
        });
        // 清空输出结果
        this.resultPanel.onDestroy();
        // 清空异常信息
        this.exPanel.body.update('');
    },
    
    initLayout: function(){
        this.items = [this.wsAddress, this.method, this.panel]
    }
});
