// menu.js
window.onload = function() {
    var role = localStorage.getItem('role'); // Lấy role từ localStorage

    if (role === 'USER') {
        document.getElementById('menuDangNhap').style.display = 'none';
        document.getElementById('menuDangKy').style.display = 'none';
        document.getElementById('menuProfile').style.display = 'block';
        document.getElementById('menuDangXuat').style.display = 'block';
    } else  {
        document.getElementById('menuDangNhap').style.display = 'block';
        document.getElementById('menuDangKy').style.display = 'block';
        document.getElementById('menuProfile').style.display = 'none';
        document.getElementById('menuDangXuat').style.display = 'none';
    }
};
