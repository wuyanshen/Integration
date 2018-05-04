Ext.ns('Ai.ws')

Ai.ws.ParamPanel = Ext.extend(Ext.Panel, {
    paramTypeAndName: '参数类型:{0}  参数名称:{1}',
    layout: 'form',
    labelWidth: 220,
    cls: 'panel_param',
    initComponent: function(){
        this.initTitle();
        this.initParam();
        
        this.initLayout();
        Ai.ws.ParamPanel.superclass.initComponent.call(this);
    },
    
    initTitle: function(){
        if (this.jsonData.children && this.jsonData.children.length > 0) {
            if (this.jsonData.type == 'array') {//数组
                this.label = this.createArrayPanel(this.jsonData);
            }
            else {//复杂对象
                this.label = this.createLabel(this.jsonData);
            }
        }
        else {//基本类型
            this.label = this.createTextField(this.jsonData);
        }
    },
    //创建panel自身
    createLabel: function(obj){
        var result = new Ext.form.Label({
            text: String.format(this.paramTypeAndName, obj.type, obj.name)
        });
        result.type = obj.type;
        result.name = obj.name;
        
        return result;
    },
    //创建数组panel
    createArrayPanel: function(obj){
        var self = this;
        //label
        var label = new Ext.form.Label({
            text: String.format(this.paramTypeAndName, obj.type, obj.name)
        });
        //addButton
        var addButton = new Ext.Button({
            text: '+',
            listeners: {
                click: function(){
                    if (self.childPanels && self.childPanels.length > 0) {//数组中为复杂对象
                        var nextChild = self.childPanels[0].cloneConfig();
                        nextChild.type = self.childPanels[0].type;
                        nextChild.name = self.childPanels[0].name;
                        nextChild.getValue = self.childPanels[0].getValue;
                        
                        self.childPanels.push(nextChild);
                        self.paramCt.items.items[1].add(nextChild);
                    }
                    else 
                        if (self.childTextFields && self.childTextFields.length > 0) {//数组中为简单对象
                            var nextChild = self.childTextFields[0].cloneConfig();
                            nextChild.type = self.childTextFields[0].type;
                            nextChild.name = self.childTextFields[0].name;
                            nextChild.getValue = self.childTextFields[0].getValue;
                            
                            self.childTextFields.push(nextChild);
                            self.paramCt.items.items[1].add(nextChild);
                        }
                    self.doLayout();
                }
            }
        });
        //reduceButton
        var reduceButton = new Ext.Button({
            text: '-',
            listeners: {
                click: function(){
                    if (self.childPanels && self.childPanels.length > 0) {//数组中为复杂对象
                        self.childPanels.pop();
                        
                        var p = self.paramCt.items.items[1];
                        var lastPanelId = p.items.items[p.items.length - 1].getId();
                        p.items.removeAt(p.items.length - 1);
                        jQuery('#' + lastPanelId).remove();
                    }
                    else 
                        if (self.childTextFields && self.childTextFields.length > 0) {//数组中为简单对象
                            self.childTextFields.pop();
                            
                            var p = self.paramCt.items.items[1];
                            var lastPanelId = p.items.items[p.items.length - 1].getId();
                            p.items.removeAt(p.items.length - 1);
                            jQuery('#' + lastPanelId).parent().parent().remove();
                        }
                }
            }
        });
        //panel
        var result = new Ext.Panel({
            layout: 'column',
            items: [{
                items: [label]
            }, {
                width: 60,
                items: [addButton]
            }, {
                width: 60,
                items: [reduceButton]
            }]
        });
        result.type = obj.type;
        result.name = obj.name;
        
        return result;
    },
    //无子结点，且不是数组，则创建textfield
    createTextField: function(obj){
        var result = new Ext.form.TextField({
            fieldLabel: String.format(this.paramTypeAndName, obj.type, obj.name),
            width: 150,
            value: obj.value
        });
        result.name = obj.name;
        result.type = obj.type;
        
        return result;
    },
    //创建panel的child panel
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
                if (child.children && child.children.length > 0) {
                    var paramPanel = new Ai.ws.ParamPanel({
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
        //组装result对象本身的name和value
        var result = {
            type: this.label.type,
            name: this.label.name
        };
        if (this.label.isXType('textfield')) {
            result.value = this.label.getValue();
        }
        //组装result对象的children的name和value
        var children = [];
        result.children = children;
        if (this.childPanels.length > 0) {
            for (var i = 0, len = this.childPanels.length; i < len; i++) {
                children.push(this.childPanels[i].getValue());
            }
        }
        if (this.childTextFields.length > 0) {
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
    //克隆方法
    clone: function(obj){
        var cloneObj = obj.cloneConfig();
        cloneObj.name = obj.name;
        cloneObj.type = obj.type;
        return obj;
    },
    initLayout: function(){
        this.items = [this.label];
        if (this.paramCt) {
            this.items.push(this.paramCt);
        }
    }
});
Ext.reg('parampanel', Ai.ws.ParamPanel);
