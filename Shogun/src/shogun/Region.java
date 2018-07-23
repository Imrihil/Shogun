/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.util.Map;

/**
 *
 * @author Imrihil
 */
public class Region {

    private final Map<String, Province> provinces;

    public Region(Map<String, Province> provinces) {
        this.provinces = provinces;
    }

    /**
     * @return the provinces
     */
    public Map<String, Province> getProvinces() {
        return provinces;
    }
}
