//
// Created by Andreas Grimm on 09/11/2021.
//

#include "include/SystemUtils.h"
#include <iomanip>
#include <sys/types.h>
#include <sys/sysctl.h>
#include <unistd.h>

unsigned long SystemUtils::getFreeMemory() {
    int mib[2] = { CTL_HW, HW_MEMSIZE };
    u_int uiNameLen = sizeof(mib) / sizeof(mib[0]);
    uint64_t uiSize;
    size_t lLen = sizeof(uiSize);

    if (sysctl(mib, uiNameLen, &uiSize, &lLen, NULL, 0) < 0) {
        perror("sysctl");
    }

    return uiSize;
}

unsigned long long SystemUtils::getTotalSystemMemory() {
    long pages = sysconf(_SC_PHYS_PAGES);
    long page_size = sysconf(_SC_PAGE_SIZE);
    return pages * page_size;
}