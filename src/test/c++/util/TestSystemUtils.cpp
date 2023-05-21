#include <iostream>
#include <iomanip>

#include "../../../main/c++/util/include/SystemUtils.h"

void testMemory() {
    long lMaxMemory = SystemUtils::getTotalSystemMemory();
    long lFreeMemory = SystemUtils::getFreeMemory();

    std::cout << " Maximum memory (Bytes): " << std::setw(8) << lMaxMemory << std::endl;
    std::cout << " Free memory (KBytes): " << std::setw(8) << lFreeMemory << std::endl;
}

int main() {
    testMemory();
    return 0;
}
