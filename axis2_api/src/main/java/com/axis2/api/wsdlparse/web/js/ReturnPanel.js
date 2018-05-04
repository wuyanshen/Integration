Ext.ns('Ai.ws')

Ai.ws.ReturnPanel = Ext.extend(Ext.Panel, {
    paramTypeAndName: '参数类型:{0}  参数名称:{1}',
    layout: 'form',
    labelWidth: 220,
    cls: 'panel_param',
    initComponent: function(){
        this.initTitle();
        this.initParam();
        
        this.initLayout();
        Ai.ws.ReturnPanel.superclass.initComponent.call(this);
    },
    
    initTitle: function(){
        if (this.jsonData.children && this.jsonData.children.length > 0) {//复杂对象
             this.label = this.createLabel(this.jsonData);
        }
        else {//基本类型
            this.label = this.createTextField(this.jsonData);
        }
    },
    //有子结点的obj创建label
    createLabel: function(obj){
        var result = new Ext.form.Label({
            text: String.format(this.paramTypeAndName, obj.type, obj.name)
        });
        result.type = obj.type;
        result.name = obj.name;
        
        return result;
    },
    //无子结点，且不是数组，则创建textfield
    createTextField: function(obj){
        var result = null;
        if (!obj.type || !obj.name) {//返回的是基本类型
            result = new Ext.form.TextField({
                fieldLabel: '返回值',
                width: 150,
                value: obj.value
            });
        }
        else {
            result = new Ext.form.TextField({
                fieldLabel: String.format(this.paramTypeAndName, obj.type, obj.name),
                width: 150,
                value: obj.value
            });
        }
        result.name = obj.name;
        result.type = obj.type;
        
        return result;
    },
    //初始化参数
    initParam: function(){
        this.childPanels = [];
        this.childTextFields = [];
        if (this.jsonData.children && this.jsonData.children.length > 0) {
            var children = this.jsonData.children, child;
            var p = new Ext.Panel({
                labelWidth: 200,
                layout: 'form',
                columnWidth: 0.9
            });
            for (var i = 0; i < children.length; i++) {
                child = children[i];
                if (child.children) {
                    var paramPanel = new Ai.ws.ReturnPanel({
                        jsonData: child
                    });
                    this.childPanels.push(paramPanel);
                    p.add(paramPanel);
                }
                else {
                    var textfield = this.createTextField(child);
                    this.childTextFields.push(textfield);
                    p.add(textfield);
                }
            }
            this.paramCt = new Ext.Panel({
                layout: 'column',
                items: [{
                    width: 30
                }, p]
            });
        }
    },
    
    getValue: function(){
        var result = {
            type: this.label.type,
            name: this.label.name
        };
        if (this.label.isXType('textfield')) {
            result.value = this.label.getValue();
        }
        if (this.childPanels.length > 0) {
            var children = [];
            result.children = children;
            for (var i = 0, len = this.childPanels.length; i < len; i++) {
                children.push(this.childPanels[i].getValue());
            }
        }
        else {
            var children = [];
            result.children = children;
            for (var i = 0, len = this.childTextFields.length; i < len; i++) {
                var obj = {
                    value: this.childTextFields[i].getValue(),
                    name: this.childTextFields[i].name,
                    type: this.childTextFields[i].type
                };
                children.push(obj);
            }
        }
        return result;
    },
    
    initLayout: function(){
        this.items = [this.label];
        if (this.paramCt) {
            this.items.push(this.paramCt);
        }
    }
});
Ext.reg('returnpanel', Ai.ws.ReturnPanel);
