/*
 * File: app.js
 *
 * This file was generated by Sencha Architect version 3.0.4.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Sencha Touch 2.3.x library, under independent license.
 * License of Sencha Architect does not include license for Sencha Touch 2.3.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

// @require @packageOverrides
Ext.Loader.setConfig({

});


Ext.application({

    requires: [
        'Ext.MessageBox'
    ],
    models: [
#foreach($entity in $app.entityList)
        '${entity.name}'#if( $velocityHasNext ),
#end#end

    ],
    stores: [
#foreach($entity in $app.entityList)
        '${entity.name}JsonPStore'#if( $velocityHasNext ),
#end#end

    ],
    views: [
        'MainView',
#foreach($entity in $app.entityList)
        '${entity.name}NavigationView',
        '${entity.name}List',
        '${entity.name}FormPanel'#if( $velocityHasNext ),
#end#end

    ],
    controllers: [
#foreach($entity in $app.entityList)
        '${entity.name}Controller'#if( $velocityHasNext ),
#end#end

    ],
    name: '${app.name}',

    getGenericServerMessage: function() {
        return "Connectivity Error, try again later.";
    },

    launch: function() {

        Ext.create('${app.name}.view.MainView', {fullscreen: true});
    }

});
