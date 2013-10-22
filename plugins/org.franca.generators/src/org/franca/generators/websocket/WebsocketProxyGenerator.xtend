/*******************************************************************************
* Copyright (c) 2013 itemis AG (http://www.itemis.de).
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.franca.generators.websocket

import org.franca.core.franca.FInterface

import static extension org.franca.generators.websocket.WebsocketGeneratorUtils.*

class WebsocketProxyGenerator {
	
	def getProxyName (FInterface api) {
		api.name.toFirstUpper + "Proxy"
	}

	def generate (FInterface api) '''
		/*
			Proxy class for client-side websocket communication.
			JS code generated by Franca's WebsocketProxyGenerator.
		*/

		function �api.proxyName�() {
			// attributes of proxy object
			// <none yet>
		}

		�api.proxyName�.prototype.connect = function(_settings) {
			this.settings = _settings;
			
			// create WebSocket for this proxy
			this.connection = new WebSocket(this.settings.host);
			this.connection.onopen = function () {};

			// store reference for this proxy in the WebSocket object
			this.connection.proxy = this;

			// message handler
			this.connection.onmessage = function (message) {
				var msg = JSON.parse(message.data);
				switch (msg.tag) {
					�FOR m : api.methods.filter[fireAndForget==null]�
					case "�m.name�":
						this.proxy.reply�m.name.toFirstUpper�(�m.outArgs.genArgList("msg.")�);
						break;
	            	�ENDFOR�
					�FOR b : api.broadcasts�
					case "�b.name�":
						this.proxy.signal�b.name.toFirstUpper�(�b.outArgs.genArgList("msg.")�);
						break;
	            	�ENDFOR�
				};
			};
		};

		�FOR m : api.methods�
		�api.proxyName�.prototype.call�m.name.toFirstUpper� = function(�m.inArgs.genArgList("")�) {
			try {
				var data = {
					tag : "�m.name�",
					�FOR a : m.inArgs SEPARATOR ","�
					�a.name� : �a.name�
					�ENDFOR�
				};
				var encoded = JSON.stringify(data);
				this.connection.send(encoded);
			}
			catch (e) {
				console.log(e);                
			}
		};
		
		�ENDFOR�

		�api.types.genEnumerations(false)�

	'''
	
}
