#include <iostream>
#include <unistd.h>
#include <iomanip>
#include <sys/types.h>
#include <sys/sysctl.h>

#include "util/include/SystemUtils.h"

unsigned long getFreeMemory() {
    int mib[2] = { CTL_HW, HW_MEMSIZE };
    u_int uiNameLen = sizeof(mib) / sizeof(mib[0]);
    uint64_t uiSize;
    size_t lLen = sizeof(uiSize);

    if (sysctl(mib, uiNameLen, &uiSize, &lLen, NULL, 0) < 0) {
        perror("sysctl");
    }

    return uiSize;
}

unsigned long long getTotalSystemMemory() {
    long pages = sysconf(_SC_PHYS_PAGES);
    long page_size = sysconf(_SC_PAGE_SIZE);
    return pages * page_size;
}

void banner() {
    long lMaxMemory = getTotalSystemMemory();
    long lFreeMemory = getFreeMemory();

    std::cout << "  #####  ######      ######     #     #####  ###  #####     " << std::endl;
    std::cout << " #     # #     #     #     #   # #   #     #  #  #     #    GriCom Basic Compiler Runtime, C++ Version 0.0.1" << std::endl;
    std::cout << " #       #     #     #     #  #   #  #        #  #          (c) Copyright A.Grimm 2021" << std::endl;
    std::cout << " #  #### #     # ### ######  #     #  #####   #  #          " << std::endl;
    std::cout << " #     # #     #     #     # #######       #  #  #          Maximum memory (Bytes): " << std::setw(8) << lMaxMemory << std::endl;
    std::cout << " #     # #     #     #     # #     # #     #  #  #     #    Free memory (KBytes): " << std::setw(8) << lFreeMemory << std::endl;
    std::cout << "  #####  ######      ######  #     #  #####  ###  #####     "<< std::endl;
}

int main() {
    banner();
    return 0;
}
