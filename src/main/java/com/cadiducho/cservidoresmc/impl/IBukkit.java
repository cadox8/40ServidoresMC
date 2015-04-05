package com.cadiducho.cservidoresmc.impl;

import com.cadiducho.cservidoresMC.cmd.ReloadCMD;
import com.cadiducho.cservidoresMC.cmd.UpdateCMD;
import com.cadiducho.cservidoresMC.cmd.VoteCMD;
import com.cadiducho.cservidoresMC.util.Inventario;
import com.cadiducho.cservidoresMC.util.Metodos;
import com.cadiducho.cservidoresMC.util.Updater;
import com.cadiducho.cservidoresmc.cServidoresMC;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cadiducho
 * 
 * Implementación para Bukkit, Spigot y Glowstone
 */

public class IBukkit extends JavaPlugin implements cServidoresMC {
    
    private final Metodos met = new Metodos(this);
    private Updater updater;
    private final Inventario inv = new Inventario(this);
    
    private final VoteCMD voteCMD = new VoteCMD(this);
    private final UpdateCMD updateCMD = new UpdateCMD(this);
    private final ReloadCMD reloadCMD = new ReloadCMD(this);
    
    private final String tag = "&8[&b40ServidoresMC&8]";
    public int configVer = 0;
    public final int configActual = 2;
    public boolean comandosCustom = true;
    public boolean premioFisico = true;
    public List<String> listaComandos;
    public List<String> listaItems;
    
    @Override
    public void onEnable() {
        debugLog("Modo Debug activado en el plugin");
        /*
         * Generar y cargar Config.yml
         */
        debugLog("Cargando configuración...");
        cargarConfig();
        /*
         * Comandos y eventos
         */
        PluginManager pluginManager = this.getServer().getPluginManager();
        debugLog("Registrando comandos y eventos...");
        
        this.getCommand("vote40").setExecutor(voteCMD);
        this.getCommand("update40").setExecutor(updateCMD);
        this.getCommand("reload40").setExecutor(reloadCMD);
        
        /*
         * Finalizar...
         */
        updater = new Updater(this, this.getVersion());
        debugLog("Checkeando nuevas versiones...");
        String actualizacion = updater.checkearVersion();
        if (actualizacion != null) {
            if (!actualizacion.equalsIgnoreCase("version actualizada")) { 
                log(actualizacion);
            }
        }
        log("Plugin 40ServidoresMC v"+this.getVersion()+" cargado completamente");
    }
    
    public void cargarConfig() {
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
                log("Generando archivo config.yml correctamente");
            } catch (Exception e) {
                this.getLogger().info("Fallo al generar el config.yml!");
                debugLog("Causa: "+e.toString());
            }
        }
        configVer = this.getConfig().getInt("configVer", configVer);
        if (configVer == 0) { 
        } else if (configVer < configActual) {
            log(Level.SEVERE, "Tu configuración es de una versión más antigua a la de este plugin!"
                + "Corrigelo o podrás tener errores..." );
        }
        comandosCustom = this.getConfig().getBoolean("comandosCustom.activado", comandosCustom);
        premioFisico = this.getConfig().getBoolean("premioFisico.activado", premioFisico);
        
        if (comandosCustom) {
            try {
                listaComandos = this.getConfig().getStringList("comandosCustom.comandos");
            } catch (NullPointerException e) {
                log(Level.WARNING, "No se ha podido cargar los premios de comandos customizados! (Error Config)");
                comandosCustom = false;
            }    
        }
        
        if (premioFisico) {
            try {
                listaItems = this.getConfig().getStringList("premioFisico.items");
            } catch (NullPointerException e) {
                log(Level.WARNING, "No se ha podido cargar los premios fisicos! (Error Config)");
                premioFisico = false;
            }
        }
        
    }
    
    @Override
    public boolean isDebug() {
        return this.getConfig().getBoolean("debug");
    }
    
    @Override
    public void debugLog(String s) {
        if (isDebug()){
            getLogger().log(Level.INFO, "[Debug] {0}", s);
        }
    }
    
    @Override
    public void log(String s) {
        getLogger().log(Level.INFO, s);
    }
    
    @Override
    public void log(Level l, String s){
        getLogger().log(l, s);
    }
    
    @Override
    public Metodos getMetodos() {
        return this.met;
    }
    
    @Override
    public String getVersion(){
        PluginDescriptionFile f = this.getDescription();
        return f.getVersion();
    }
    
    @Override
    public String getTag() {
        return this.tag;     
    }
    
    @Override
    public Updater getUpdater() {
        return this.updater;
    }
    
    @Override
    public Inventario getInv() {
        return this.inv;
    }

    @Override
    public IBukkit getInstance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}