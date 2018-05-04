Ext.onReady(function() {
			var main = new Ai.ws.MainPanel({
						jsonData : jsonData,
						renderTo : Ext.getBody()
					});
			main.resultPanel.render(Ext.getBody())
			main.exPanel.render(Ext.getBody())
			main.repaintParamPanel();

		});