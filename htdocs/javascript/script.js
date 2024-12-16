
let storeMap = L.map("map_space");
storeMap.setView([42.6384261, 12.674297], 6);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 25,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(storeMap);


function findAppleStores() {
    for(let i = 0; i < Apple_Stores.length; i++) {
        let store_marker = L.marker([Apple_Stores[i].latitude, Apple_Stores[i].longitude]);
        store_marker.addTo(storeMap);
        let store_info = "<strong>" + Apple_Stores[i].name + "</strong> <br> <i>" + Apple_Stores[i].address + "</i>";
        updateStoreList(store_info.replace("<br>", " "));
        store_marker.bindPopup(store_info);
    }
}

function updateStoreList(info) {
    let store_list = document.getElementById("store_address_list");
    let new_store = document.createElement("li");
    new_store.innerHTML = info;
    new_store.classList.add("mb-3");
    store_list.appendChild(new_store);
}